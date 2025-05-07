package acme.features.customer.recommendation;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendations.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	private CustomerRecommendationRepository repository;

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		String city = super.getRequest().getData("city", String.class);
		Collection<Recommendation> recommendations = this.repository.findRecommendationsByCity(city);

		if (recommendations.isEmpty())
			recommendations = this.fetchAndSaveRecommendations(city);

		super.getBuffer().addData(recommendations);
	}

	private Collection<Recommendation> fetchAndSaveRecommendations(final String city) {
		String prompt = String.format(
				"Give me a list of 5 recommendations (experiences, activities, restaurants, accommodation, or other interesting things) for the city of %s. Respond in JSON format as an array, each with fields: title, description, category, location, city.",
				city);

		// Gemini 2.0 Flash API endpoint and key (replace with your actual endpoint/key)
		String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=YOUR_API_KEY";
		String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		try {
			// Parse the JSON from Gemini's response (you may need to adjust this based on
			// actual response structure)
			String json = this.extractJsonFromGeminiResponse(response.getBody());
			Recommendation[] recs = new ObjectMapper().readValue(json, Recommendation[].class);
			for (Recommendation rec : recs)
				this.repository.save(rec);
			return Arrays.asList(recs);
		} catch (Exception e) {
			// Handle or log the exception as needed
			throw new RuntimeException("Failed to fetch or parse recommendations", e);
		}
	}

	private String extractJsonFromGeminiResponse(final String responseBody) {
		// Implement logic to extract the JSON array from Gemini's response
		// This will depend on the actual API response format
		// For example, you may need to parse a field like
		// "candidates[0].content.parts[0].text"
		// Use a JSON library or manual parsing as needed
		return responseBody; // Placeholder
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "title", "description", "category", "location", "city");
		super.getResponse().addData(dataset);
	}
}
