
package acme.features.customer.recommendation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendation.Recommendation;
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
		Collection<Recommendation> recommendations = this.repository.findRecommendations();
		super.getBuffer().addData(recommendations);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "city", "state", "country", "formatted", "openingHours", "url");
		super.addPayload(dataset, recommendation, "id");
		super.getResponse().addData(dataset);
	}
}
