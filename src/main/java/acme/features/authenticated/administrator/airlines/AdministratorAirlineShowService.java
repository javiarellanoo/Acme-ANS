
package acme.features.authenticated.administrator.airlines;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineType;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline Airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		Airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(Airline);
	}

	@Override
	public void unbind(final Airline Airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AirlineType.class, Airline.getType());

		dataset = super.unbindObject(Airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
