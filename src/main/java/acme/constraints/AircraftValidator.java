
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (aircraft == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (aircraft.getRegistrationNumber() != null) {
			boolean uniqueIdentifier = !this.repository.countSameRegistrationNumber(aircraft.getRegistrationNumber()).equals(0L);
			super.state(context, uniqueIdentifier, "registrationNumber", "acme.validation.aircraft.registrationNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
