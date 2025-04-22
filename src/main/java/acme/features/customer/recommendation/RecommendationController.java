
package acme.features.customer.recommendation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.recommendations.Recommendation;
import acme.realms.Customer;

@GuiController
public class RecommendationController extends AbstractGuiController<Customer, Recommendation> {

	@Autowired
	protected RecommendationListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
