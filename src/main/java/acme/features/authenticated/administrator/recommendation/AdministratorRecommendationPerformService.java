
package acme.features.authenticated.administrator.recommendation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
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
		Collection<Booking> bookings = this.repository.findDistinctCities();
		Collection<Recommendation> existingRecs = this.repository.findAllRecommendations();
		Set<String> recommendedCityCountry = existingRecs.stream().map(r -> r.getCity() + "," + r.getCountry()).collect(Collectors.toSet());

		for (Booking booking : bookings) {
			String city = booking.getFlight().getDestinationCity();
			if (city.equals("Not defined yet"))
				continue;

			String country = booking.getFlight().getDestinationCountry() != null ? booking.getFlight().getDestinationCountry() : "";
			String key = city + "," + country;
			if (!recommendedCityCountry.contains(key)) {
				Collection<Recommendation> recommendations = this.fetchAndSaveRecommendations(city, country);
				super.getBuffer().addData(recommendations);
			}
		}
	}

	private Collection<Recommendation> fetchAndSaveRecommendations(final String city, final String country) {
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
				if (url.isEmpty()) {
					url = feature.path("properties").path("datasource").path("raw").path("website").asText("");
				}
				if (url.isEmpty()) {
					url = feature.path("properties").path("datasource").path("raw").path("url").asText("");
				}
				if (url.isEmpty()) {
					url = feature.path("properties").path("datasource").path("url").asText("");
				}
				if (url.isEmpty()) {
					url = feature.path("properties").path("url").asText("");
				}
				if (url.isEmpty()) {
					url = "not available";
				}
				rec.setUrl(url);
				recs[i] = rec;
				this.repository.save(rec);
			}
			return Arrays.asList(recs);
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch or parse recommendations", e);
		}
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "state", "city", "country", "openingHours", "formatted", "url");
		super.getResponse().addData(dataset);
	}
}
