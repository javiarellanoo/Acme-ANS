
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
		else if (booking.getLocatorCode() != null) {
			Booking existingBooking = this.repository.getBookingByLocatorCode(booking.getLocatorCode());
			boolean uniqueLocatorCode = UniquenessHelper.checkUniqueness(existingBooking, booking);
			super.state(context, uniqueLocatorCode, "uniqueLocatorCode", "acme.validation.booking.uniqueLocatorCode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
