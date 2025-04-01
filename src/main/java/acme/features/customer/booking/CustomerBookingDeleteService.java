
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Booking booking;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingkById(id);

		status = booking != null && booking.getDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.getCustomer().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingkById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(booking, "locatorCode", "purchaseMoment", "price", "lastCardNibble");
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {
	}

	@Override
	public void perform(final Booking booking) {
		Collection<BookingRecord> relationships;

		relationships = this.repository.findRelationshipsByBooking(booking.getId());

		this.repository.deleteAll(relationships);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelChoices;
		SelectChoices flightChoices;

		Dataset dataset;

		Collection<Flight> flights = this.repository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "id", booking.getFlight());

		travelChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "purchaseMoment", "price", "lastCardNibble", "draftMode");
		dataset.put("travelClass", travelChoices.getSelected().getKey());
		dataset.put("travelClasses", travelChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
