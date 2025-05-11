
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Booking booking;
		Customer customer;

		int bookingId = super.getRequest().getData("id", int.class);

		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingId);

		booking = bookingRecord.getBooking();
		customer = booking == null ? null : bookingRecord.getBooking().getCustomer();
		status = booking != null && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		Booking booking;
		int bookingRecordId;
		boolean isDraftMode;

		bookingRecordId = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		booking = bookingRecord.getBooking();
		isDraftMode = booking.getDraftMode();

		super.getBuffer().addData(bookingRecord);
		super.getResponse().addGlobal("isDraftMode", isDraftMode);
		super.getResponse().addGlobal("bookingId", booking.getId());
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		SelectChoices passengersChoices;
		Dataset dataset;
		Collection<Passenger> passengers;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		passengers = this.repository.findAllMyPassengers(customerId);
		passengersChoices = SelectChoices.from(passengers, "displayString", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("bookingId", bookingRecord.getBooking().getId());
		dataset.put("passengerId", passengersChoices.getSelected().getKey());

		super.getResponse().addGlobal("passengers", passengersChoices);
		super.getResponse().addData(dataset);
	}
}
