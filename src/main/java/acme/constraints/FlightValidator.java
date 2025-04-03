
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Autowired
	private LegRepository repository;


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		List<Leg> legs = this.repository.findAllLegsByFlightId(flight.getId());

		Boolean validFlight = true;
		if (flight.getDraftMode().equals(true))
			validFlight = true;
		else if (legs.size() == 0)
			validFlight = false;
		else if (legs.size() == 1)
			validFlight = true;
		else
			for (int i = 1; i < legs.size(); i++) {
				Date previousArrival = legs.get(i - 1).getScheduledArrival();
				Date currentDeparture = legs.get(i).getScheduledDeparture();

				validFlight = validFlight && MomentHelper.isAfter(currentDeparture, previousArrival);
			}
		super.state(context, validFlight, "scheduledArrival", "acme.validation.flight.legs.message");

		result = !super.hasErrors(context);

		return result;
	}

}
