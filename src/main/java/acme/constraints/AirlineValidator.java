
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airline == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (airline.getIataCode() != null) {
			boolean uniqueIataCode = !this.repository.countSameIataCode(airline.getIataCode()).equals(0);
			super.state(context, uniqueIataCode, "IATACode", "acme.validation.airline.iataCode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
