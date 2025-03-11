
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		Boolean matches;
		String initials;
		DefaultUserIdentity identity;

		identity = manager.getIdentity();

		initials = "";

		initials += identity.getName().trim().charAt(0);

		initials += identity.getSurname().trim().charAt(0);

		matches = manager.getIdentifier().trim().startsWith(initials);
		super.state(context, matches, "identifier", "acme.validation.manager.identifier.message");
		result = !super.hasErrors(context);
		return result;
	}

}
