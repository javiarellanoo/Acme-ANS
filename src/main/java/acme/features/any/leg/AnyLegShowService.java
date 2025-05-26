
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;

@GuiService
public class AnyLegShowService extends AbstractGuiService<Any, Leg> {

	@Autowired
	private AnyLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Leg leg;
		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(masterId);
		if (leg == null)
			status = false;
		else {
			flight = this.repository.findFlightById(leg.getFlight().getId());
			status = flight != null && !flight.getDraftMode() && !leg.getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		SelectChoices choicesAircraft;
		Collection<Aircraft> aircrafts;
		Collection<Airport> airports;
		SelectChoices choicesStatus;
		SelectChoices choicesDepartureAirport;
		SelectChoices choicesDestinationAirport;
		aircrafts = this.repository.findAircraftsByAirlineId(leg.getFlight().getAirline().getId());
		airports = this.repository.findAllAirports();
		choicesAircraft = SelectChoices.from(aircrafts, "model", leg.getAircraft());
		choicesDepartureAirport = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		choicesDestinationAirport = SelectChoices.from(airports, "name", leg.getDestinationAirport());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset = super.unbindObject(leg, "flightNumber", "draftMode", "status", "departureAirport.name", "destinationAirport.name", "duration", "scheduledDeparture", "scheduledArrival", "aircraft.model");
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("destinationAirport", choicesDestinationAirport.getSelected().getKey());
		dataset.put("destinationAirports", choicesDestinationAirport);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
