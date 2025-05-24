
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

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
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean legStatus;
		boolean memberStatus;
		int assignmentId;
		FlightAssignment assignment;
		int legId;
		Leg leg;
		Collection<Leg> validLegs;
		int fcmId;
		FlightCrewMember fcm;
		Collection<FlightCrewMember> validMembers;
		int memberId;
		int airlineId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);

		if (assignment == null)
			status = false;
		else if (!assignment.getDraftMode() || assignment.getFlightCrewMember().getAirline().getId() != airlineId)
			status = false;
		else if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			validLegs = this.repository.findLegsByAirlineId(airlineId);
			validMembers = this.repository.findCrewMembersByAirlineId(airlineId);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			legStatus = legId == 0 || leg != null && validLegs.contains(leg);
			fcmId = super.getRequest().getData("flightCrewMember", int.class);
			fcm = this.repository.findCrewMemberById(fcmId);
			memberStatus = fcmId == 0 || fcm != null && validMembers.contains(fcm);

			status = legStatus && memberStatus;
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
		int memberId;

		legId = super.getRequest().getData("leg", int.class);
		memberId = super.getRequest().getData("flightCrewMember", int.class);
		Leg leg = this.repository.findLegById(legId);
		FlightCrewMember member = this.repository.findCrewMemberById(memberId);
		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
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

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks", "duty", "status", "draftMode");
		dataset.put("legId", legChoices.getSelected().getKey());
		dataset.put("flightCrewMemberId", memberChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", memberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", memberChoices);

		super.getResponse().addData(dataset);
	}
}
