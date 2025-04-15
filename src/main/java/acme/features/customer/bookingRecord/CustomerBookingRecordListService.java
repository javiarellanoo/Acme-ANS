
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<BookingRecord> bookingRecords;
		int bookingId;
		boolean isDraftMode;

		bookingId = super.getRequest().getData("bookingId", int.class);
		isDraftMode = this.repository.findBookingById(bookingId).getDraftMode();
		bookingRecords = this.repository.findAllBookingRecordsByBookingId(bookingId);

		super.getBuffer().addData(bookingRecords);
		super.getResponse().addGlobal("isDraftMode", isDraftMode);
		super.getResponse().addGlobal("bookingId", bookingId);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "passenger.fullName", "passenger.passportNumber");
		super.addPayload(dataset, bookingRecord, "passenger.birthDate");
		super.getResponse().addData(dataset);
	}
}
