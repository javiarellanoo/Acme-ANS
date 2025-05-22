
package acme.features.authenticated.administrator.recommendation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
import acme.entities.recommendation.Recommendation;

@GuiService
public class AdministratorRecommendationPerformService extends AbstractGuiService<Administrator, Recommendation> {

	@Autowired
	private AdministratorRecommendationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Recommendation recommendation;

		recommendation = new Recommendation();

		super.getBuffer().addData(recommendation);
	}

	@Override
	public void validate(final Recommendation recommendation) {

	}

	@Override
	public void perform(final Recommendation recommendation) {
		String city = super.getRequest().getData("city", String.class);
		String country = super.getRequest().getData("country", String.class);
		Collection<Recommendation> recs = this.computeRecommendation(city, country);
		Recommendation rec = recs.stream().findFirst().orElse(null);
		if (rec == null) {
			recommendation.setName(null);
			recommendation.setState(null);
			recommendation.setCity(null);
			recommendation.setCountry(null);
			recommendation.setOpeningHours(null);
			recommendation.setFormatted(null);
			recommendation.setUrl(null);
		} else {
			recommendation.setName(rec.getName());
			recommendation.setState(rec.getState());
			recommendation.setCity(rec.getCity());
			recommendation.setCountry(rec.getCountry());
			recommendation.setOpeningHours(rec.getOpeningHours());
			recommendation.setFormatted(rec.getFormatted());
			recommendation.setUrl(rec.getUrl());
		}
	}

	private Collection<Recommendation> computeRecommendation(final String city, final String country) {
		assert city != null;
		assert country != null;
		Collection<Recommendation> recommendation;
		if (SpringHelper.isRunningOn("production"))
			recommendation = this.computeLiveRecommendations(city, country);
		else
			recommendation = this.computeMockedRecommendation(city, country);
		return recommendation;
	}

	private Collection<Recommendation> computeMockedRecommendation(final String city, final String country) {
		Recommendation rec = new Recommendation();
		rec.setName("Mocked Place");
		rec.setState("Mocked State");
		rec.setCity(city != null && !city.isEmpty() ? city : "Not available");
		rec.setCountry(country != null && !country.isEmpty() ? country : "Not available");
		rec.setOpeningHours("09:00-18:00");
		rec.setFormatted("123 Mocked St, Mock City");
		rec.setUrl("http://mocked-url.com");
		this.repository.save(rec);
		return Collections.singletonList(rec);
	}

	private Collection<Recommendation> computeLiveRecommendations(final String city, final String country) {
		Collection<Recommendation> result;
		String apiKey = "1ebf282bc72a47c2a584be663584b6c3";
		String location = city + (country != null && !country.isEmpty() ? ", " + country : "");
		String geocodeUrl = String.format("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", location, apiKey);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> geocodeResponse = restTemplate.getForEntity(geocodeUrl, String.class);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode geocodeRootNode = objectMapper.readTree(geocodeResponse.getBody());
			JsonNode features = geocodeRootNode.path("features");

			if (features.isEmpty())
				return Collections.emptyList();

			JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
			String lon = coordinates.get(0).asText();
			String lat = coordinates.get(1).asText();

			int radius = 5000;
			String placesUrl = String.format("https://api.geoapify.com/v2/places?categories=tourism.sights&filter=circle:%s,%s,%d&limit=5&apiKey=%s", lon, lat, radius, apiKey);

			ResponseEntity<String> placesResponse = restTemplate.getForEntity(placesUrl, String.class);
			JsonNode placesRootNode = objectMapper.readTree(placesResponse.getBody());
			JsonNode placesFeatures = placesRootNode.path("features");

			Recommendation[] recs = new Recommendation[placesFeatures.size()];
			for (int i = 0; i < placesFeatures.size(); i++) {
				JsonNode feature = placesFeatures.get(i);
				Recommendation rec = new Recommendation();
				rec.setName(feature.path("properties").path("name").asText("Not available"));
				String state = feature.path("properties").path("state").asText("");
				if (state.isEmpty())
					state = feature.path("properties").path("suburb").asText("");
				if (state.isEmpty())
					state = "Not available";
				rec.setState(state);
				rec.setCity(city != null && !city.isEmpty() ? city : "Not available");
				rec.setCountry(country != null && !country.isEmpty() ? country : "Not available");
				rec.setOpeningHours(feature.path("properties").path("opening_hours").asText("Not available"));
				rec.setFormatted(feature.path("properties").path("formatted").asText("Not available"));
				String url = feature.path("properties").path("website").asText("");
				if (url.isEmpty())
					url = feature.path("properties").path("datasource").path("raw").path("website").asText("");
				if (url.isEmpty())
					url = feature.path("properties").path("datasource").path("raw").path("url").asText("");
				if (url.isEmpty())
					url = feature.path("properties").path("datasource").path("url").asText("");
				if (url.isEmpty())
					url = feature.path("properties").path("url").asText("");
				if (url.isEmpty())
					url = "not available";
				rec.setUrl(url);
				recs[i] = rec;
				this.repository.save(rec);
			}
			result = Arrays.asList(recs);
			MomentHelper.sleep(1000);
		} catch (Exception e) {
			super.state(false, "*", "administration.recommendation.form.label.api-error");
			return Collections.emptyList();
		}
		return result;
	}

	@Override
	public void bind(final Recommendation recommendation) {
		super.bindObject(recommendation, "city", "country");
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "state", "city", "country", "openingHours", "formatted", "url");
		super.getResponse().addData(dataset);
	}

}
