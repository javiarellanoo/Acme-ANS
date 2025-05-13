
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
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

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
		customer = bookingRecord != null && booking == null ? null : bookingRecord.getBooking().getCustomer();
		status = bookingRecord != null && bookingRecord.getBooking() != null && bookingRecord.getBooking().getDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

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

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		SelectChoices passengersChoices;
		Dataset dataset;
		Collection<Passenger> passengers;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		passengers = this.repository.findPassengersNotInBooking(customerId, bookingRecord.getBooking().getId());
		passengersChoices = SelectChoices.from(passengers, "displayString", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "passenger");
		dataset.put("passengerId", passengersChoices.getSelected().getKey());
		dataset.put("passengers", passengersChoices);

		super.getResponse().addData(dataset);
	}
}
