
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
		String bookingIdStr;
		String passengerIdStr;

		if (super.getRequest().getMethod().equals("GET")) {
			bookingIdStr = super.getRequest().getData("bookingId", String.class);
			bookingId = Integer.parseInt(bookingIdStr);
			booking = this.repository.findBookingById(bookingId);
			status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());
		} else {
			bookingIdStr = super.getRequest().getData("bookingId", String.class);
			passengerIdStr = super.getRequest().getData("passenger", String.class);

			try {
				bookingId = Integer.parseInt(bookingIdStr);
				passengerId = Integer.parseInt(passengerIdStr);
				booking = this.repository.findBookingById(bookingId);
				passenger = this.repository.findPassengerById(passengerId);
				passengers = this.repository.findPassengersNotInBooking(super.getRequest().getPrincipal().getActiveRealm().getId(), bookingId);

				boolean validBooking = booking != null && booking.getDraftMode() && booking.getCustomer() != null;
				boolean validCustomer = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());
				boolean validPassenger = passenger != null && passengers != null && passengers.contains(passenger) && !passenger.getDraftMode();

				if (passengerId == 0)
					status = validBooking && validCustomer;
				else
					status = validBooking && validPassenger && validCustomer;

			} catch (AssertionError | Exception e) {
				status = false;
			}
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

		passengers = passengers.stream().filter(x -> !x.getDraftMode()).toList();
		passengersChoices = SelectChoices.from(passengers, "displayString", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "passenger");
		dataset.put("passengerId", passengersChoices.getSelected().getKey());
		dataset.put("passengers", passengersChoices);

		super.getResponse().addData(dataset);
	}
}
