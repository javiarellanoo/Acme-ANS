
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Booking booking;
		int bookingRecordId;
		BookingRecord bookingRecord;
		bookingRecordId = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(bookingRecordId);

		if (bookingRecord == null)
			status = false;
		else {
			booking = this.repository.findBookingById(bookingRecord.getBooking().getId());
			status = booking.getDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && !super.getRequest().getMethod().equals("GET");
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("id", int.class);

		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingId);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.delete(bookingRecord);
	}

}
