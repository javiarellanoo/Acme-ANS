
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRepository;
import acme.helpers.UniquenessHelper;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	// ConstraintValidator Interface ----------------------------------------------------------------------------

	@Autowired
	private BookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (booking == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (booking.getLocatorCode() != null) {
				Booking existingBooking = this.repository.getBookingByLocatorCode(booking.getLocatorCode());
				boolean uniqueLocatorCode = UniquenessHelper.checkUniqueness(existingBooking, booking);
				super.state(context, uniqueLocatorCode, "locatorCode", "acme.validation.booking.uniqueLocatorCode.message");
			}
			if (!booking.getDraftMode()) {
				Boolean hasLocatorCode = booking.getLocatorCode() != null && !booking.getLocatorCode().isBlank();
				super.state(context, hasLocatorCode, "locatorCode", "acme.validation.booking.locatorCode.message");

				Boolean hasCreditCardNibble = booking.getLastCardNibble() != null && !booking.getLastCardNibble().isBlank();
				super.state(context, hasCreditCardNibble, "lastCardNibble", "acme.validation.booking.lastCardNibble.message");

				Boolean hasPassengers = this.repository.countNumberOfPassengers(booking.getId()).compareTo(0L) > 0;
				super.state(context, hasPassengers, "*", "acme.validation.booking.passengers.message");
			}
		}

		result = !super.hasErrors(context);

		return result;

	}
}
