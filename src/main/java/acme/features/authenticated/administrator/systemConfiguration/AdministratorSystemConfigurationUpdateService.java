
package acme.features.authenticated.administrator.systemConfiguration;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfiguration.SystemConfiguration;

@GuiService
public class AdministratorSystemConfigurationUpdateService extends AbstractGuiService<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorSystemConfigurationRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfiguration config;

		config = this.repository.getSystemConfig();

		super.getBuffer().addData(config);
	}

	@Override
	public void bind(final SystemConfiguration config) {
		super.bindObject(config, "systemCurrency", "acceptedCurrencies");
	}

	@Override
	public void validate(final SystemConfiguration config) {

	}

	@Override
	public void perform(final SystemConfiguration config) {
		this.repository.save(config);
	}

	@Override
	public void unbind(final SystemConfiguration config) {
		Dataset dataset;

		dataset = super.unbindObject(config, "systemCurrency", "acceptedCurrencies");

		super.getResponse().addData(dataset);
	}
}
