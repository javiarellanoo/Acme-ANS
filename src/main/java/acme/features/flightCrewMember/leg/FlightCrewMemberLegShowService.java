
package acme.features.flightCrewMember.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberLegShowService extends AbstractGuiService<FlightCrewMember, Leg> {

	@Autowired
	private FlightCrewMemberLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		int memberId;
		Collection<FlightAssignment> assignments;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignments = this.repository.findAssignmentsByLegAndMemberId(legId, memberId);
		status = leg != null && !assignments.isEmpty();

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
