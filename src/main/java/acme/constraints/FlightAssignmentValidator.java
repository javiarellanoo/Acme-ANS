
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.flightAssignments.FlightAssignmentRepository;
import acme.realms.AvailabilityStatus;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment assignment, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assignment == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean notCompletedLeg;
			Date currentMoment = MomentHelper.getCurrentMoment();

			notCompletedLeg = assignment.getDraftMode().equals(true) || MomentHelper.isAfter(assignment.getLeg().getScheduledArrival(), currentMoment);

			super.state(context, notCompletedLeg, "leg", "acme.validation.flight-assignment.completed-leg.message");

			boolean availableMember;

			availableMember = assignment.getDraftMode().equals(true) || assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

			super.state(context, availableMember, "flightCrewMember", "acme.validation.flight-assignment.not-available-crew-member.message");

			boolean onlyOnePilot;
			FlightAssignment pilotAssignment;

			pilotAssignment = this.repository.findPilotAssignmentsByLegId(assignment.getLeg().getId());
			onlyOnePilot = assignment.getDraftMode().equals(true) || pilotAssignment == null || pilotAssignment.equals(assignment);

			super.state(context, onlyOnePilot, "duty", "acme.validation.flight-assignment.pilot-already-assigned.message");

			boolean onlyOneCopilot;
			FlightAssignment copilotAssignment;

			copilotAssignment = this.repository.findCopilotAssignmentsByLegId(assignment.getLeg().getId());
			onlyOneCopilot = assignment.getDraftMode().equals(true) || copilotAssignment == null || copilotAssignment.equals(assignment);

			super.state(context, onlyOneCopilot, "duty", "acme.validation.flight-assignment.copilot-already-assigned.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
