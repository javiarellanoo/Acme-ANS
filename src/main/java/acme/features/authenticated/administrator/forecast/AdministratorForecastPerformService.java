
package acme.features.authenticated.administrator.forecast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.forecast.Forecast;

@GuiService
public class AdministratorForecastPerformService extends AbstractGuiService<Administrator, Forecast> {

	@Autowired
	private AdministratorForecastRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Forecast forecast;

		forecast = new Forecast();

		super.getBuffer().addData(forecast);

	}

	@Override
	public void bind(final Forecast forecast) {
		super.bindObject(forecast, "city", "predictionMoment");
	}

	@Override
	public void validate(final Forecast forecast) {

	}

	@Override
	public void perform(final Forecast forecast) {
		String city;
		Date predictionMoment;
		Forecast current;

		city = super.getRequest().getData("city", String.class);
		predictionMoment = super.getRequest().getData("predictionMoment", Date.class);
		current = this.computeForecast(city, predictionMoment);
		if (current == null) {
			forecast.setMaximumTemperature(null);
			forecast.setMinimumTemperature(null);
			forecast.setPrecipitationSum(null);
			forecast.setWeatherCode(null);
			forecast.setWindGusts(null);
			forecast.setWindSpeed(null);

		} else {
			forecast.setMaximumTemperature(current.getMaximumTemperature());
			forecast.setMinimumTemperature(current.getMinimumTemperature());
			forecast.setPrecipitationSum(current.getPrecipitationSum());
			forecast.setWeatherCode(current.getWeatherCode());
			forecast.setWindGusts(current.getWindGusts());
			forecast.setWindSpeed(current.getWindSpeed());
		}

		this.repository.save(forecast);
	}

	@Override
	public void unbind(final Forecast forecast) {
		Dataset dataset;

		dataset = super.unbindObject(forecast, "city", "predictionMoment", "maximumTemperature", "minimumTemperature", "precipitationSum", "weatherCode", "windGusts", "windSpeed");

		super.getResponse().addData(dataset);
	}

	protected Forecast computeForecast(final String city, final Date predictionMoment) {
		assert city != null;
		assert predictionMoment != null;

		Forecast result;

		if (SpringHelper.isRunningOn("development") || SpringHelper.isRunningOn("production"))
			result = this.computeLiveForecast(city, predictionMoment);
		else
			result = this.computeMockedForecast(city, predictionMoment);

		return result;
	}

	protected Forecast computeMockedForecast(final String city, final Date predictionMoment) {
		assert city != null;
		assert predictionMoment != null;

		Forecast result = new Forecast();

		result.setCity(city);
		result.setPredictionMoment(predictionMoment);

		result.setWeatherCode(61);
		result.setMaximumTemperature(10.0);
		result.setMinimumTemperature(1.0);
		result.setPrecipitationSum(0.5);
		result.setWindGusts(23.4);
		result.setWindSpeed(10.0);

		return result;
	}

	protected Forecast computeLiveForecast(final String city, final Date predictionMoment) {
		assert city != null;
		assert predictionMoment != null;

		try {
			String latitudeAndLongitude = this.findLatitudeAndLongitude(city);
			if (latitudeAndLongitude == null) {
				super.state(false, "*", "administrator.forecast.not-found");
				return null;
			}

			Forecast forecast = this.fetchForecast(city, latitudeAndLongitude, predictionMoment);

			if (forecast == null)
				super.state(false, "*", "administrator.forecast.not-found");

			MomentHelper.sleep(2000);
			return forecast;
		} catch (Throwable oops) {
			super.state(false, "*", "administration.forecast.form.label.api-error");
			return null;
		}
	}

	private String findLatitudeAndLongitude(final String city) throws IOException {
		String apiKey = "1ebf282bc72a47c2a584be663584b6c3";
		String location = city;
		String geocodeUrl = String.format("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", location, apiKey);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> geocodeResponse = restTemplate.getForEntity(geocodeUrl, String.class);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode geocodeRootNode = objectMapper.readTree(geocodeResponse.getBody());
			JsonNode features = geocodeRootNode.path("features");

			if (features.isEmpty())
				return null;

			JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
			String lon = coordinates.get(0).asText();
			String lat = coordinates.get(1).asText();
			return lat + " " + lon;
		} catch (Exception e) {
			super.state(false, "*", "administration.forecast.form.label.api-error");
			return null;
		}
	}

	private Forecast fetchForecast(final String city, final String latitudeAndLongitude, final Date predictionMoment) {
		String[] coords = latitudeAndLongitude.split(" ");
		String latitude = coords[0];
		String longitude = coords[1];

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(predictionMoment);

		String url = String.format(
			"https://historical-forecast-api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&start_date=%s&end_date=%s&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max,wind_gusts_10m_max", latitude,
			longitude, dateStr, dateStr);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		String body = response.getBody();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(body);

			JsonNode dailyNode = rootNode.get("daily");

			if (dailyNode != null) {
				JsonNode weatherCodeArray = dailyNode.get("weather_code");
				JsonNode tempMaxArray = dailyNode.get("temperature_2m_max");
				JsonNode tempMinArray = dailyNode.get("temperature_2m_min");
				JsonNode precipitationArray = dailyNode.get("precipitation_sum");
				JsonNode windSpeedArray = dailyNode.get("wind_speed_10m_max");
				JsonNode windGustsArray = dailyNode.get("wind_gusts_10m_max");

				if (weatherCodeArray != null && weatherCodeArray.isArray() && weatherCodeArray.size() > 0 && tempMaxArray != null && tempMaxArray.isArray() && tempMaxArray.size() > 0 && tempMinArray != null && tempMinArray.isArray()
					&& tempMinArray.size() > 0 && precipitationArray != null && precipitationArray.isArray() && precipitationArray.size() > 0 && windSpeedArray != null && windSpeedArray.isArray() && windSpeedArray.size() > 0 && windGustsArray != null
					&& windGustsArray.isArray() && windGustsArray.size() > 0) {

					Forecast forecast = new Forecast();

					forecast.setCity(city);

					forecast.setWeatherCode(weatherCodeArray.get(0).asInt());
					forecast.setPredictionMoment(predictionMoment);
					forecast.setMaximumTemperature(tempMaxArray.get(0).asDouble());
					forecast.setMinimumTemperature(tempMinArray.get(0).asDouble());
					forecast.setPrecipitationSum(precipitationArray.get(0).asDouble());
					forecast.setWindSpeed(windSpeedArray.get(0).asDouble());
					forecast.setWindGusts(windGustsArray.get(0).asDouble());

					return forecast;
				}
			}

			throw new RuntimeException("Error parsing weather API response: missing or invalid daily data");

		} catch (Exception e) {
			throw new RuntimeException("Error processing weather API response", e);
		}
	}

}
