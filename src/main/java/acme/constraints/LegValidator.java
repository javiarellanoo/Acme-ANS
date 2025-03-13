
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.legs.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		Boolean matches;
		String airlineCode = leg.getFlight().getAirline().getIataCode();
		String flightNumber = leg.getFlightNumber();

		matches = flightNumber.trim().startsWith(airlineCode.trim());
		super.state(context, matches, "flightCode", "acme.validation.leg.flightCode.message");

		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();
		Date minimumArrival = MomentHelper.deltaFromMoment(scheduledDeparture, 1, ChronoUnit.MINUTES);
		Boolean validArrival = MomentHelper.isAfterOrEqual(scheduledArrival, minimumArrival);
		super.state(context, validArrival, "scheduledArrival", "acme.validation.leg.scheduledArrival.message");

		result = !super.hasErrors(context);
		return result;
	}
}
