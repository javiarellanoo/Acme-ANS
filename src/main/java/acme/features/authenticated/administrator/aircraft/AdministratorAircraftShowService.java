
package acme.features.authenticated.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftShowService extends AbstractGuiService<Administrator, Aircraft> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		status = aircraft != null;

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices statusChoices;
		SelectChoices airlineChoices;
		Dataset dataset;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();

		statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());
		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("statuses", statusChoices);
		dataset.put("status", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
