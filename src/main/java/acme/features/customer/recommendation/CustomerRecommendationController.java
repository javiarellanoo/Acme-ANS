
package acme.features.customer.recommendation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.recommendation.Recommendation;
import acme.realms.Customer;

@GuiController
public class CustomerRecommendationController extends AbstractGuiController<Customer, Recommendation> {

	@Autowired
	private CustomerRecommendationShowService	showService;

	@Autowired
	private CustomerRecommendationListService	listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
