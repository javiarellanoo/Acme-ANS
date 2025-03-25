
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;
import acme.realms.repositories.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Autowired
	private ManagerRepository repository;


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;
		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (manager.getIdentifier() != null) {

			boolean uniqueIdentifier = this.repository.countSameIdentifier(manager.getIdentifier()).equals(1);
			super.state(context, uniqueIdentifier, "identifier", "acme.validation.manager.identifierNonUnique.message");

			Boolean matches;
			String initials;
			DefaultUserIdentity identity;
			identity = manager.getIdentity();

			initials = "";

			initials += identity.getName().trim().charAt(0);

			initials += identity.getSurname().trim().charAt(0);

			matches = manager.getIdentifier().trim().startsWith(initials);
			super.state(context, matches, "identifier", "acme.validation.manager.identifier.message");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
