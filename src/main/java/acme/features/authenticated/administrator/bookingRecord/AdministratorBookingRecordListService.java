
package acme.features.authenticated.administrator.bookingRecord;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;

@GuiService
public class AdministratorBookingRecordListService extends AbstractGuiService<Administrator, BookingRecord> {

	@Autowired
	private AdministratorBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);

		Booking booking = this.repository.findBookingById(bookingId);
		status = booking != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<BookingRecord> bookingRecords;
		Integer bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);

		Booking booking = this.repository.findBookingById(bookingId);
		if (booking != null)
			bookingRecords = this.repository.findAllBookingRecordsByBookingId(bookingId);
		else
			bookingRecords = Collections.emptyList();

		super.getBuffer().addData(bookingRecords);
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
