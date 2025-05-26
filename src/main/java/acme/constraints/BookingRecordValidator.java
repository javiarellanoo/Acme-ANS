
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.BookingRepository;
import acme.helpers.UniquenessHelper;

@Validator
public class BookingRecordValidator extends AbstractValidator<ValidBookingRecord, BookingRecord> {

	// ConstraintValidator Interface
	// ----------------------------------------------------------------------------

	@Autowired
	private BookingRepository repository;


	@Override
	protected void initialise(final ValidBookingRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final BookingRecord bookingRecord, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (bookingRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (bookingRecord.getBooking() != null && bookingRecord.getPassenger() != null) {
			BookingRecord bookingRecordObtained = this.repository.findBookingRecordByPassengerAndBooking(bookingRecord.getPassenger().getId(), bookingRecord.getBooking().getId());
			boolean uniquePassengerInBooking = UniquenessHelper.checkUniqueness(bookingRecordObtained, bookingRecord);

			super.state(context, uniquePassengerInBooking, "*", "acme.validation.bookingRecord.uniqueBookingRecord.message");

		}

		result = !super.hasErrors(context);

		return result;

	}
}
