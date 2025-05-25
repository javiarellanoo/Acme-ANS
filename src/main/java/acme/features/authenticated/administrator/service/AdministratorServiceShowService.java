
package acme.features.authenticated.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AdministratorServiceShowService extends AbstractGuiService<Administrator, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorServiceRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id;
		Service service;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);

		status = service != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Service service;
		int id;

		id = super.getRequest().getData("id", int.class);
		service = this.repository.findServiceById(id);

		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "pictureLink", "averageDwellTime", "promotionCode", "discountMoney");

		super.getResponse().addData(dataset);
	}
}
