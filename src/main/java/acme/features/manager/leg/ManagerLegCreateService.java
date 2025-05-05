
package acme.features.manager.leg;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

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
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		int aircraftId;
		Aircraft aircraft;
		int destinationAirportId;
		Airport destinationAirport;
		int departureAirportId;
		Airport departureAirport;
		boolean aircraftStatus;
		boolean departureAirportStatus;
		boolean destinationAirportStatus;
		if (super.getRequest().getMethod().equals("GET")) {
			masterId = super.getRequest().getData("masterId", int.class);
			flight = this.repository.findFlightById(masterId);
			status = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager());
		} else {
			masterId = super.getRequest().getData("masterId", int.class);
			flight = this.repository.findFlightById(masterId);
			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findValidAircraftById(aircraftId, flight.getAirline().getId());
			aircraftStatus = aircraftId == 0 || aircraft != null;
			destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
			destinationAirport = this.repository.findAirportById(destinationAirportId);
			departureAirportId = super.getRequest().getData("departureAirport", int.class);
			departureAirport = this.repository.findAirportById(departureAirportId);
			destinationAirportStatus = destinationAirportId == 0 || destinationAirport != null;
			departureAirportStatus = departureAirportId == 0 || departureAirport != null;
			status = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && aircraftStatus && departureAirportStatus && destinationAirportStatus;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		String iataCode = flight.getAirline().getIataCode();
		Date scheduledDeparture = MomentHelper.getCurrentMoment();
		Date scheduledArrival = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.MINUTES);
		leg = new Leg();
		leg.setStatus(LegStatus.ON_TIME);
		leg.setScheduledArrival(scheduledArrival);
		leg.setScheduledDeparture(scheduledDeparture);
		leg.setFlightNumber(iataCode);
		leg.setDraftMode(true);
		leg.setFlight(flight);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int destinationAirportId;
		int aircraftId;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		aircraftId = super.getRequest().getData("aircraft", int.class);
		destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);
		Airport destinationAirport = this.repository.findAirportById(destinationAirportId);
		super.bindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival");
		leg.setDraftMode(true);
		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setDestinationAirport(destinationAirport);
	}

	@Override
	public void validate(final Leg leg) {
		boolean validDate;
		Date currentMoment = MomentHelper.getCurrentMoment();
		if (leg.getScheduledDeparture() != null) {
			validDate = MomentHelper.isAfterOrEqual(leg.getScheduledDeparture(), currentMoment);
			super.state(validDate, "scheduledDeparture", "acme.validation.leg.scheduledDeparture");
		}

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
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
		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival");
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("draftMode", true);
		dataset.put("validDate", false);
		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("destinationAirport", choicesDestinationAirport.getSelected().getKey());
		dataset.put("destinationAirports", choicesDestinationAirport);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
