
package acme.features.customer.recommendation;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

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
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<String> cities = this.repository.findDistinctCitiesByCustomerId(customerId).stream().map(x -> x.getFlight().getDestinationCity()).distinct().toList();

		Collection<Recommendation> recommendations = new ArrayList<>();

		for (String city : cities) {
			Collection<Recommendation> existingRecommendations = this.repository.findRecommendationsByCity(city);
			if (existingRecommendations.isEmpty()) {
				Recommendation newRecommendation = new Recommendation();
				newRecommendation.setCity(city);
				recommendations.add(newRecommendation);
			} else
				recommendations.addAll(existingRecommendations);
		}

		super.getBuffer().addData(recommendations);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "city");
		super.getResponse().addData(dataset);
	}
}
