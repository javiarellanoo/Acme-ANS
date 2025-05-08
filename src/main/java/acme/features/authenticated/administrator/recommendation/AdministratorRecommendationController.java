
package acme.features.authenticated.administrator.recommendation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.recommendation.Recommendation;

@GuiController
public class AdministratorRecommendationController extends AbstractGuiController<Administrator, Recommendation> {

	@Autowired
	private AdministratorRecommendationPerformService performService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("perform", this.performService);
	}
}
