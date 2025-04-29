
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
		boolean memberStatus;
		int assignmentId;
		FlightAssignment assignment;
		int legId;
		Leg leg;
		int fcmId;
		FlightCrewMember fcm;
		int memberId;
		int airlineId;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		legStatus = legId == 0 || leg != null;
		fcmId = super.getRequest().getData("flightCrewMember", int.class);
		fcm = this.repository.findCrewMemberById(fcmId);
		memberStatus = fcmId == 0 || fcm != null;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
		status = assignment != null && assignment.getDraftMode() && assignment.getFlightCrewMember().getAirline().getId() == airlineId && legStatus && memberStatus;

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
		int memberId;

		legId = super.getRequest().getData("leg", int.class);
		memberId = super.getRequest().getData("flightCrewMember", int.class);
		Leg leg = this.repository.findLegById(legId);
		FlightCrewMember member = this.repository.findCrewMemberById(memberId);
		super.bindObject(assignment, "duty", "lastUpdate", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
	}

	@Override
	public void validate(final FlightAssignment assignment) {

		if (assignment.getLeg() != null && assignment.getFlightCrewMember() != null) {
			boolean notCompletedLeg;
			Date currentMoment = MomentHelper.getCurrentMoment();

			notCompletedLeg = MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), currentMoment);

			super.state(notCompletedLeg, "leg", "acme.validation.flight-assignment.completed-leg.message");

			boolean availableMember;

			availableMember = assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

			super.state(availableMember, "flightCrewMember", "acme.validation.flight-assignment.not-available-crew-member.message");

			boolean notSimultaneousAssignment;
			Collection<FlightAssignment> assignments = this.repository.findAssignmenstByCrewMemberId(assignment.getFlightCrewMember().getId());

			notSimultaneousAssignment = true;

			for (FlightAssignment a : assignments)
				if (!(MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), a.getLeg().getScheduledDeparture()) || MomentHelper.isBefore(a.getLeg().getScheduledArrival(), assignment.getLeg().getScheduledDeparture())))
					notSimultaneousAssignment = false;

			super.state(notSimultaneousAssignment, "leg", "acme.validation.flight-assignment.simultaneous-leg.message");

			boolean onlyOnePilot;
			FlightAssignment pilotAssignment;

			pilotAssignment = this.repository.findPilotAssignmentsByLegId(assignment.getLeg().getId());
			onlyOnePilot = pilotAssignment == null || pilotAssignment.equals(assignment) || !assignment.getDuty().equals(Duty.PILOT);

			super.state(onlyOnePilot, "duty", "acme.validation.flight-assignment.pilot-already-assigned.message");

			boolean onlyOneCopilot;
			FlightAssignment copilotAssignment;

			copilotAssignment = this.repository.findCopilotAssignmentsByLegId(assignment.getLeg().getId());
			onlyOneCopilot = copilotAssignment == null || copilotAssignment.equals(assignment) || !assignment.getDuty().equals(Duty.COPILOT);

			super.state(onlyOneCopilot, "duty", "acme.validation.flight-assignment.copilot-already-assigned.message");
		} else
			super.state(false, "*", "acme.validation.flight-assignment.null-leg-or-flight.message");

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
		Collection<FlightCrewMember> members;
		SelectChoices memberChoices;
		int memberId;
		int airlineId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
		legs = this.repository.findLegsByAirlineId(airlineId);
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		members = this.repository.findCrewMembersByAirlineId(airlineId);
		memberChoices = SelectChoices.from(members, "employeeCode", assignment.getFlightCrewMember());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks", "status", "duty", "draftMode");
		dataset.put("legId", legChoices.getSelected().getKey());
		dataset.put("flightCrewMemberId", memberChoices.getSelected().getKey());
		dataset.put("onlyOneCopilot", false);
		dataset.put("onlyOnePilot", false);
		dataset.put("notSimultaneousAssignment", false);
		dataset.put("availableMember", false);
		dataset.put("notCompletedLeg", false);
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", memberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", memberChoices);

		super.getResponse().addData(dataset);
	}

}
