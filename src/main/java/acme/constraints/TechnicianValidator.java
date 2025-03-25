
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technician;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (technician == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean matches;
			String initials;
			DefaultUserIdentity identity;

			identity = technician.getIdentity();

			initials = "";

			initials += identity.getName().trim().charAt(0);

			initials += identity.getSurname().trim().charAt(0);

			matches = technician.getLicenseNumber().trim().startsWith(initials);
			super.state(context, matches, "licenseNumber", "acme.validation.technician.licenseNumber.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
