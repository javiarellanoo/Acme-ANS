
package acme.features.customer.recommendation;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import acme.entities.recommendations.Recommendation;

@Service
public class RecommendationService {

	private static final String	API_KEY			= "1ebf282bc72a47c2a584be663584b6c3";
	private static final String	GEOAPIFY_URL	= "https://api.geoapify.com/v2/places";
	private static final String	CATEGORIES		= "tourism.attraction,catering.restaurant,accommodation,leisure,entertainment";
	private static final int	LIMIT			= 10;

	private final HttpClient	httpClient		= HttpClient.newHttpClient();


	public List<Recommendation> getRecommendations(final String city) {
		List<Recommendation> recommendations = new ArrayList<>();
		if (city == null || city.isBlank() || city.equalsIgnoreCase("(Unknown)"))
			return recommendations;

		try {
			String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

			String uriString = String.format("%s?categories=%s&filter=text=%s&limit=%d&apiKey=%s", RecommendationService.GEOAPIFY_URL, RecommendationService.CATEGORIES, encodedCity, RecommendationService.LIMIT, RecommendationService.API_KEY);

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).GET().build();

			HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JSONObject jsonResponse = new JSONObject(response.body());
				JSONArray features = jsonResponse.optJSONArray("features");

				if (features != null)
					for (int i = 0; i < features.length(); i++) {
						JSONObject feature = features.getJSONObject(i);
						JSONObject properties = feature.optJSONObject("properties");

						if (properties != null && properties.has("name") && properties.has("formatted")) {
							Recommendation rec = new Recommendation();
							rec.setName(properties.getString("name"));
							rec.setAddress(properties.getString("formatted"));

							JSONArray categoriesArray = properties.optJSONArray("categories");
							String categoryString = "N/A";
							if (categoriesArray != null) {
								List<String> categoryList = new ArrayList<>();
								for (int j = 0; j < categoriesArray.length(); j++)
									categoryList.add(categoriesArray.getString(j));
								categoryString = categoryList.stream().filter(cat -> !cat.startsWith("details.") && !cat.startsWith("datasource.") && !cat.equals("wheelchair") && !cat.contains("building.") && !cat.contains("political."))
									.map(cat -> cat.replace("catering.", "").replace("tourism.", "").replace("accommodation.", "").replace("leisure.", "").replace("entertainment.", "")).distinct().collect(Collectors.joining(", "));
								if (categoryString.isBlank())
									categoryString = "General";
							}
							rec.setCategories(categoryString);

							recommendations.add(rec);
						}
					}
			} else
				System.err.println("Geoapify API request failed for city '" + city + "': " + response.statusCode() + " - " + response.body());

		} catch (Exception e) {
			System.err.println("Error fetching recommendations for city '" + city + "': " + e.getMessage());
		}

		return recommendations;
	}
}
