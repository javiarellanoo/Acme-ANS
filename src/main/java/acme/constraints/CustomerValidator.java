
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.helpers.UniquenessHelper;
import acme.realms.Customer;
import acme.realms.repositories.CustomerRepository;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	// ConstraintValidator Interface ----------------------------------------------------------------------------

	@Autowired
	private CustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (customer.getIdentifier() != null) {

			Customer existingCustomer = this.repository.getCustomerByIdentifier(customer.getIdentifier());
			boolean uniqueIdentifier = UniquenessHelper.checkUniqueness(existingCustomer, customer);

			super.state(context, uniqueIdentifier, "identifer", "acme.validation.customer.identifierUniqueness.message");

			boolean initialsOfTheIdentifier;

			String[] surnames = customer.getIdentity().getSurname().trim().split(" ");
			char firstSurnameInitial = surnames[0].trim().charAt(0);
			char nameInitial = customer.getIdentity().getName().trim().charAt(0);

			initialsOfTheIdentifier = customer.getIdentifier().charAt(0) == nameInitial && customer.getIdentifier().charAt(1) == firstSurnameInitial;

			super.state(context, initialsOfTheIdentifier, "identifier", "acme.validation.customer.identifier.message");
		}
		result = !super.hasErrors(context);
		return result;

	}
}
