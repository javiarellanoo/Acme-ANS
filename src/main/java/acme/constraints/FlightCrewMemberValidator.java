
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember fcm, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		boolean initialsMatch;

		String employeeCode = fcm.getEmployeeCode();

		String name = fcm.getIdentity().getName();
		String surname = fcm.getIdentity().getSurname();

		initialsMatch = name.charAt(0) == employeeCode.charAt(0) && surname.charAt(0) == employeeCode.charAt(1);

		super.state(context, initialsMatch, "employeeCode", "acme.validation.flightCrewMember.employeeCode.message");

		result = !super.hasErrors(context);

		return result;

	}
}
