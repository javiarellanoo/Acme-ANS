
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int airlineId;
		Airline airline;
		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			airlineId = super.getRequest().getData("airline", int.class);
			airline = this.repository.findAirlineById(airlineId);
			status = airlineId == 0 || airline != null;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		Manager employer;

		employer = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setManager(employer);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		flight.setAirline(airline);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(true);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Collection<Airline> airlines;
		SelectChoices choices;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "name", flight.getAirline());

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}
}
