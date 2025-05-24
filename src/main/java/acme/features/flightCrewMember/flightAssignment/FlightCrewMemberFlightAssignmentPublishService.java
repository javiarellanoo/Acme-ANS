
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	public FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean legStatus;
		int assignmentId;
		FlightAssignment assignment;
		int legId;
		Leg leg;
		Collection<Leg> validLegs;
		int memberId;
		int airlineId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);

		if (assignment == null)
			status = false;
		else if (!assignment.getDraftMode() || assignment.getFlightCrewMember().getId() != memberId)
			status = false;
		else if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			validLegs = this.repository.findLegsByAirlineId(airlineId);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			legStatus = legId == 0 || leg != null && validLegs.contains(leg);

			status = legStatus;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;

		legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		assignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		if (assignment.getLeg() != null) {
			boolean notCompletedLeg;
			Date currentMoment = MomentHelper.getCurrentMoment();

			notCompletedLeg = MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), currentMoment);

			super.state(notCompletedLeg, "leg", "acme.validation.flight-assignment.completed-leg.message");

			boolean notSimultaneousAssignment;
			Collection<FlightAssignment> assignments = this.repository.findAssignmenstByCrewMemberId(assignment.getFlightCrewMember().getId());

			notSimultaneousAssignment = true;

			for (FlightAssignment a : assignments)
				if (!(MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), a.getLeg().getScheduledDeparture()) || MomentHelper.isBefore(a.getLeg().getScheduledArrival(), assignment.getLeg().getScheduledDeparture())))
					notSimultaneousAssignment = false;

			super.state(notSimultaneousAssignment, "leg", "acme.validation.flight-assignment.simultaneous-leg.message");

			if (assignment.getDuty() != null) {
				boolean onlyOnePilot;
				FlightAssignment pilotAssignment;
				pilotAssignment = this.repository.findPilotAssignmentsByLegId(assignment.getLeg().getId());
				onlyOnePilot = pilotAssignment == null || !assignment.getDuty().equals(Duty.PILOT);
				super.state(onlyOnePilot, "duty", "acme.validation.flight-assignment.pilot-already-assigned.message");

				boolean onlyOneCopilot;
				FlightAssignment copilotAssignment;

				copilotAssignment = this.repository.findCopilotAssignmentsByLegId(assignment.getLeg().getId());
				onlyOneCopilot = copilotAssignment == null || !assignment.getDuty().equals(Duty.COPILOT);

				super.state(onlyOneCopilot, "duty", "acme.validation.flight-assignment.copilot-already-assigned.message");
			}

		}

		boolean availableMember;

		availableMember = assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

		super.state(availableMember, "*", "acme.validation.flight-assignment.not-available-crew-member.message");

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		Collection<Leg> legs;
		SelectChoices legChoices;
		int memberId;
		int airlineId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
		legs = this.repository.findLegsByAirlineId(airlineId);
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks", "status", "duty", "draftMode");
		dataset.put("legId", legChoices.getSelected().getKey());
		dataset.put("flightCrewMemberId", assignment.getFlightCrewMember().getId());
		dataset.put("onlyOneCopilot", false);
		dataset.put("onlyOnePilot", false);
		dataset.put("notSimultaneousAssignment", false);
		dataset.put("availableMember", false);
		dataset.put("notCompletedLeg", false);
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}

}
