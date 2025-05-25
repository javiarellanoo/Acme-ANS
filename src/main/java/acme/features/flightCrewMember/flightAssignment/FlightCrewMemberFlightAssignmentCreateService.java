
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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Collection<Leg> validLegs;
		int memberId;
		int airlineId;
		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
			validLegs = this.repository.findLegsByAirlineId(airlineId);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			status = legId == 0 || leg != null && validLegs.contains(leg);
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		FlightCrewMember member;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		assignment = new FlightAssignment();
		assignment.setFlightCrewMember(member);
		assignment.setDraftMode(true);
		assignment.setDuty(Duty.PILOT);
		assignment.setStatus(AssignmentStatus.PENDING);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;

		legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.bindObject(assignment, "duty", "status", "remarks");
		assignment.setDraftMode(true);
		assignment.setLeg(leg);
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
		int memberId;
		int airlineId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
		legs = this.repository.findLegsByAirlineId(airlineId);
		legChoices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		dutyChoices = SelectChoices.from(Duty.class, assignment.getDuty());
		statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());

		dataset = super.unbindObject(assignment, "lastUpdate", "remarks");
		dataset.put("duties", dutyChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("draftMode", true);

		super.getResponse().addData(dataset);
	}
}
