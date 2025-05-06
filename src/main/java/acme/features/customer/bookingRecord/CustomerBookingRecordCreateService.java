
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
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		int passengerId;
		Passenger passenger;
		Collection<Passenger> passengers;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			bookingId = super.getRequest().getData("bookingId", int.class);
			passengerId = super.getRequest().getData("passenger", int.class);

			booking = this.repository.findBookingById(bookingId);
			passenger = this.repository.findPassengerById(passengerId);
			passengers = this.repository.findPassengersNotInBooking(super.getRequest().getPrincipal().getActiveRealm().getId(), bookingId);

			boolean validBooking = booking != null && booking.getDraftMode() && booking.getCustomer() != null && booking.getCustomer().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

			boolean validPassenger = passenger != null && passengers != null && passengers.contains(passenger);

			status = validBooking && validPassenger;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);
		super.getResponse().addGlobal("bookingId", bookingId);
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
		this.repository.save(bookingRecord);
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
