
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.FlightCrewMember;
import acme.realms.repositories.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember fcm, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (fcm == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueCrewMember;
				FlightCrewMember existingCrewMember;

				existingCrewMember = this.repository.findCrewMemberByEmployeeCode(fcm.getEmployeeCode());
				uniqueCrewMember = existingCrewMember == null || existingCrewMember.equals(fcm);

				super.state(context, uniqueCrewMember, "employeeCode", "acme.validation.flightCrewMember.duplicated-employeeCode.message");

			}
			{
				boolean initialsMatch;

				String employeeCode = fcm.getEmployeeCode();

				String name = fcm.getIdentity().getName();
				String surname = fcm.getIdentity().getSurname();

				initialsMatch = name.charAt(0) == employeeCode.charAt(0) && surname.charAt(0) == employeeCode.charAt(1);

				super.state(context, initialsMatch, "employeeCode", "acme.validation.flightCrewMember.employeeCode.message");
			}
		}

		result = !super.hasErrors(context);

		return result;

	}
}
