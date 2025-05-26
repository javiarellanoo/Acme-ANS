
package acme.features.authenticated.administrator.forecast;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.forecast.Forecast;

@GuiController
public class AdministratorForecastController extends AbstractGuiController<Administrator, Forecast> {

	@Autowired
	private AdministratorForecastPerformService performService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("perform", this.performService);
	}
}
