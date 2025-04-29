
package acme.features.customer.booking;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		Integer flightId;
		Flight flight = null;
		int bookingId;
		int customerId;
		boolean flightStatus;

		Booking booking;
		Customer customer;
		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {

			bookingId = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingkById(bookingId);

			customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

			flightId = super.getRequest().getData("flight", int.class);
			if (flightId != null)
				flight = this.repository.findFlightById(flightId);

			flightStatus = flight.getId() == 0 || flight != null && !flight.getDraftMode();

			status = booking != null && booking.getDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && flightStatus;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setLocatorCode("");
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setCustomer(customer);

		booking.setLastCardNibble("");

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		booking.setDraftMode(true);
		super.bindObject(booking, "locatorCode", "travelClass", "price", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		boolean hasCreditCardNibble;
		boolean hasSomePassengers;

		hasCreditCardNibble = booking.getLastCardNibble() != null && !booking.getLastCardNibble().isBlank();
		super.state(hasCreditCardNibble, "*", "acme.validation.booking.lastCreditCardNibble.message");

		hasSomePassengers = this.repository.countNumberOfPassengers(booking.getId()).compareTo(0L) > 0;
		super.state(hasSomePassengers, "*", "acme.validation.booking.passengers.message");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClassChoices;
		SelectChoices flightChoices;

		Dataset dataset;

		Collection<Flight> flights = this.repository.findAllNotDraftFlights();
		Flight bookingFlight = booking.getFlight();

		Collection<Flight> futureFlights = flights.stream().filter(f -> f.getScheduledArrival().compareTo(MomentHelper.getCurrentMoment()) > 0).toList();

		Collection<Flight> displayFlights = new ArrayList<>(futureFlights);

		if (bookingFlight != null && !displayFlights.contains(bookingFlight))
			displayFlights.add(bookingFlight);

		flightChoices = SelectChoices.from(displayFlights, "displayString", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "price", "lastCardNibble", "draftMode");
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("travelClasses", travelClassChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
