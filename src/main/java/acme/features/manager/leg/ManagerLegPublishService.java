
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Leg leg;
		int aircraftId;
		Aircraft aircraft;
		int destinationAirportId;
		Airport destinationAirport;
		int departureAirportId;
		Airport departureAirport;
		boolean aircraftStatus;
		boolean departureAirportStatus;
		boolean destinationAirportStatus;

		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(masterId);
		flight = this.repository.findFlightById(leg.getFlight().getId());
		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findValidAircraftById(aircraftId, flight.getAirline().getId());
		aircraftStatus = aircraftId == 0 || aircraft != null;
		destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
		destinationAirport = this.repository.findAirportById(destinationAirportId);
		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.repository.findAirportById(departureAirportId);
		destinationAirportStatus = destinationAirportId == 0 || destinationAirport != null;
		departureAirportStatus = departureAirportId == 0 || departureAirport != null;
		status = flight != null && flight.getDraftMode() && leg.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && aircraftStatus && departureAirportStatus && destinationAirportStatus;

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
	public void validate(final Leg leg) {
		Collection<Leg> legsOfFlight = this.repository.findOtherLegsByFlightId(leg.getFlight().getId(), leg.getId());
		boolean validLeg = true;
		for (Leg l : legsOfFlight)
			if (MomentHelper.isBeforeOrEqual(leg.getScheduledDeparture(), l.getScheduledArrival()) && MomentHelper.isAfter(leg.getScheduledArrival(), l.getScheduledDeparture()))
				validLeg = false;
		super.state(validLeg, "scheduledDeparture", "acme.validation.leg.conflictiveLeg.message");
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int destinationAirportId;
		int aircraftId;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);
		Airport destinationAirport = this.repository.findAirportById(destinationAirportId);
		super.bindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival");
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setDestinationAirport(destinationAirport);
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
		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("validLeg", false);
		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("destinationAirport", choicesDestinationAirport.getSelected().getKey());
		dataset.put("destinationAirports", choicesDestinationAirport);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
