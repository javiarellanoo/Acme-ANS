
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment;

		assignment = new FlightAssignment();
		assignment.setDraftMode(true);

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
		assignment.setDraftMode(true);
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);

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

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks");
		dataset.put("duty", dutyChoices);
		dataset.put("status", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", memberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", memberChoices);
		dataset.put("draftMode", true);

		super.getResponse().addData(dataset);
	}
}
