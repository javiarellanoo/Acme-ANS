
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
import acme.features.authenticated.administrator.airlines.AdministratorAirlineRepository;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository	repository;

	@Autowired
	private AdministratorAirlineRepository	airlineRepository;


	@Override
	public void authorise() {
		boolean authorised = true;
		int aircraftId = 0;
		int airlineId = 0;
		Aircraft aircraft = null;
		Airline airline = null;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		authorised = aircraft != null;

		airlineId = super.getRequest().getData("airline", int.class);
		if (airlineId != 0) {
			airline = this.airlineRepository.findAirlineById(airlineId);
			authorised = authorised && airline != null;

		}
		super.getResponse().setAuthorised(authorised);

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
	public void bind(final Aircraft aircraft) {
		Airline airline;
		int airlineId;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.airlineRepository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		aircraft.setAirline(airline);
	}
	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {

		SelectChoices statusChoices;
		SelectChoices airlineChoices;
		Dataset dataset;
		Collection<Airline> airlines;

		airlines = this.airlineRepository.findAllAirlines();

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
