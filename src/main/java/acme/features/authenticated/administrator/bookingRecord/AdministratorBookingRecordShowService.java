
package acme.features.authenticated.administrator.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.features.authenticated.administrator.booking.AdministratorBookingRepository;

@GuiService
public class AdministratorBookingRecordShowService extends AbstractGuiService<Administrator, BookingRecord> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		int bookingRecordId;
		BookingRecord bookingRecord;
		boolean status;
		bookingRecordId = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		status = bookingRecord != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int bookingRecordId;

		bookingRecordId = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(bookingRecordId);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		if (bookingRecord.getPassenger() != null)
			dataset.put("passenger", bookingRecord.getPassenger().getDisplayString());
		super.getResponse().addData(dataset);
	}
}
