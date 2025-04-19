
package acme.features.any.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;

@GuiService
public class AnyFlightShowService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		status = flight != null && !flight.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Airline> airlines;
		airlines = this.repository.findAllAirlines();

		choices = SelectChoices.from(airlines, "name", flight.getAirline());
		dataset = super.unbindObject(flight, "tag", "draftMode", "requiresSelfTransfer", "cost", "description", "originCity", "destinationCity", "scheduledDeparture", "scheduledArrival", "numberOfLayovers", "manager.identity.fullName", "airline.name");
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}

}
