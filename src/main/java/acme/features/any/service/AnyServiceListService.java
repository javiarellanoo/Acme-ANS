
package acme.features.any.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AnyServiceListService extends AbstractGuiService<Any, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Service> services;

		services = this.repository.findAllServices();

		super.getBuffer().addData(services);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "pictureLink", "averageDwellTime");

		super.getResponse().addData(dataset);
	}
}
