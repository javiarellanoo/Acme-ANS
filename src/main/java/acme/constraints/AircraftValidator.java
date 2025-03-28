
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;
import acme.helpers.UniquenessHelper;

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
			Aircraft existingAircraft = this.repository.getCustomerByIdentifier(aircraft.getRegistrationNumber());
			boolean uniqueLocatorCode = UniquenessHelper.checkUniqueness(existingAircraft, aircraft);
			super.state(context, uniqueLocatorCode, "registrationNumber", "acme.validation.aircraft.registrationNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
