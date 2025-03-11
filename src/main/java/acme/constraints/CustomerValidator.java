
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;

import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	// ConstraintValidator Interface ----------------------------------------------------------------------------

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;


		boolean initialsOfTheIdentifier;

		String[] surnames = customer.getIdentity().getSurname().trim().split(" ");
		char firstSurnameInitial = surnames[0].trim().charAt(0);
		char nameInitial = customer.getIdentity().getName().trim().charAt(0);

		initialsOfTheIdentifier = customer.getIdentifier().charAt(0) == nameInitial && customer.getIdentifier().charAt(1) == firstSurnameInitial;

		super.state(context, initialsOfTheIdentifier, "identifier", "acme.validation.customer.identifier.message");


		result = !super.hasErrors(context);

		return result;
	}
}
