
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.aircrafts.Aircraft;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.helpers.UniquenessHelper;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Autowired
	private LegRepository repository;


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (!StringHelper.isBlank(leg.getFlightNumber())) {

				Leg existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				Boolean uniqueFlightNumber = UniquenessHelper.checkUniqueness(existingLeg, leg);
				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.leg.flightNumberUnique.message");

				Boolean matches;
				String airlineCode = leg.getFlight().getAirline().getIataCode();
				String flightNumber = leg.getFlightNumber();

				matches = flightNumber.trim().startsWith(airlineCode.trim());
				super.state(context, matches, "flightNumber", "acme.validation.leg.flightNumber.message");
			}
			Date scheduledDeparture = leg.getScheduledDeparture();
			Date scheduledArrival = leg.getScheduledArrival();
			if (scheduledArrival != null && scheduledDeparture != null) {
				Date minimumArrival = MomentHelper.deltaFromMoment(scheduledDeparture, 1, ChronoUnit.MINUTES);
				Boolean validArrival = MomentHelper.isAfterOrEqual(scheduledArrival, minimumArrival);
				super.state(context, validArrival, "scheduledArrival", "acme.validation.leg.scheduledArrival.message");

				if (leg.getAircraft() != null) {
					Aircraft aircraft = leg.getAircraft();
					Collection<Leg> legsWithSameAircraft = this.repository.findLegsByAircraftId(aircraft.getId(), leg.getId());
					boolean validAircraft = true;
					for (Leg l : legsWithSameAircraft)
						if (MomentHelper.isBefore(leg.getScheduledDeparture(), l.getScheduledArrival()) && MomentHelper.isAfter(leg.getScheduledArrival(), l.getScheduledDeparture()))
							validAircraft = false;
					super.state(context, validAircraft, "aircraft", "acme.validation.leg.occupiedAircraft.message");
				}
			}

			if (leg.getFlight() != null && leg.getDraftMode() == false) {
				Flight flight = leg.getFlight();
				Collection<Leg> legsOfFlight = this.repository.findOtherLegsByFlightId(leg.getFlight().getId(), leg.getId());
				boolean validLeg = true;
				for (Leg l : legsOfFlight)
					if (MomentHelper.isBeforeOrEqual(leg.getScheduledDeparture(), l.getScheduledArrival()) && MomentHelper.isAfterOrEqual(leg.getScheduledArrival(), l.getScheduledDeparture()))
						validLeg = false;
				super.state(context, validLeg, "scheduledDeparture", "acme.validation.leg.conflictiveLeg.message");

			}
		}
		result = !super.hasErrors(context);
		return result;
	}
}
