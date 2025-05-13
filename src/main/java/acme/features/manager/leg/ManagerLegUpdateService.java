
package acme.features.manager.leg;

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
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

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
		if (leg == null)
			status = false;
		else {
			flight = this.repository.findFlightById(leg.getFlight().getId());
			if (!leg.getDraftMode() || !super.getRequest().getPrincipal().hasRealm(flight.getManager()))
				status = false;
			else if (super.getRequest().getMethod().equals("GET"))
				status = true;
			else {
				aircraftId = super.getRequest().getData("aircraft", int.class);
				aircraft = this.repository.findAircraftById(aircraftId);
				aircraftStatus = aircraftId == 0 || aircraft != null;
				destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
				destinationAirport = this.repository.findAirportById(destinationAirportId);
				departureAirportId = super.getRequest().getData("departureAirport", int.class);
				departureAirport = this.repository.findAirportById(departureAirportId);
				destinationAirportStatus = destinationAirportId == 0 || destinationAirport != null;
				departureAirportStatus = departureAirportId == 0 || departureAirport != null;
				status = flight != null && flight.getDraftMode() && leg.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && aircraftStatus && departureAirportStatus && destinationAirportStatus;
			}
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
		aircrafts = this.repository.findAllAircrafts();
		airports = this.repository.findAllAirports();
		choicesAircraft = SelectChoices.from(aircrafts, "model", leg.getAircraft());
		choicesDepartureAirport = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		choicesDestinationAirport = SelectChoices.from(airports, "name", leg.getDestinationAirport());
		choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("validDate", false);
		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("destinationAirport", choicesDestinationAirport.getSelected().getKey());
		dataset.put("destinationAirports", choicesDestinationAirport);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
