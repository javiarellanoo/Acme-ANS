package acme.features.customer.recommendation;

import java.util.Collection;
import java.util.Arrays;

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
public class CustomerRecommendationShowService extends AbstractGuiService<Customer, Recommendation> {

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

        if (recommendations.isEmpty()) {
            recommendations = this.fetchAndSaveRecommendations(city);
        }

        super.getBuffer().addData(recommendations);
    }

    private Collection<Recommendation> fetchAndSaveRecommendations(final String city) {
        String prompt = String.format(
                "Give me a list of 5 recommendations (experiences, activities, restaurants, accommodation, or other interesting things) for the city of %s. Respond in JSON format as an array, each with fields: title, description, category, location, city.",
                city);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=YOUR_API_KEY";
        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        try {
            String json = this.extractJsonFromGeminiResponse(response.getBody());
            Recommendation[] recs = new ObjectMapper().readValue(json, Recommendation[].class);
            for (Recommendation rec : recs)
                this.repository.save(rec);
            return Arrays.asList(recs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse recommendations", e);
        }
    }

    private String extractJsonFromGeminiResponse(final String responseBody) {
        return responseBody; // Placeholder for actual JSON extraction logic
    }

    @Override
    public void unbind(final Recommendation recommendation) {
        Dataset dataset = super.unbindObject(recommendation, "title", "description", "category", "location", "city");
        super.getResponse().addData(dataset);
    }
}