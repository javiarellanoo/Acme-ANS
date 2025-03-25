
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technician;
import acme.realms.repositories.TechnicianRepository;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Autowired
	TechnicianRepository technicianRepository;


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
			Boolean unique;
			String initials;
			DefaultUserIdentity identity;

			identity = technician.getIdentity();

			initials = "";

			initials += identity.getName().trim().charAt(0);

			initials += identity.getSurname().trim().charAt(0);

			matches = technician.getLicenseNumber().trim().startsWith(initials);

			Technician alreadyExistingTechnician = this.technicianRepository.findTechnicianByLicenseNumber(technician.getLicenseNumber());

			unique = alreadyExistingTechnician == null || alreadyExistingTechnician.getId() == technician.getId();

			boolean condition = matches && unique;

			super.state(context, condition, "licenseNumber", "acme.validation.technician.licenseNumber.message");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
