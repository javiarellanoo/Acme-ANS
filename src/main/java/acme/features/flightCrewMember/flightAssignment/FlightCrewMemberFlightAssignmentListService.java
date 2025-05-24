
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> assignments;
		int memberId;
		boolean completedLeg;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		completedLeg = super.getRequest().getData("completed", boolean.class);

		Date currentMoment = MomentHelper.getCurrentMoment();

		if (completedLeg)
			assignments = this.repository.findAssignmentsByMemberId(memberId).stream().filter(x -> MomentHelper.isAfterOrEqual(currentMoment, x.getLeg().getScheduledArrival())).toList();
		else
			assignments = this.repository.findAssignmentsByMemberId(memberId).stream().filter(x -> MomentHelper.isBefore(currentMoment, x.getLeg().getScheduledArrival())).toList();

		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "status", "draftMode");
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		super.addPayload(dataset, assignment, "remarks");

		super.getResponse().addData(dataset);

	}

}
