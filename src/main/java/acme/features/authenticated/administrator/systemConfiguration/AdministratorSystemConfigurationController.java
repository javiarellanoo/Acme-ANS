
package acme.features.authenticated.administrator.systemConfiguration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.systemConfiguration.SystemConfiguration;

@GuiController
public class AdministratorSystemConfigurationController extends AbstractGuiController<Administrator, SystemConfiguration> {

	@Autowired
	private AdministratorSystemConfigurationShowService		showService;

	@Autowired
	private AdministratorSystemConfigurationUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("show", this.showService);
	}
}
