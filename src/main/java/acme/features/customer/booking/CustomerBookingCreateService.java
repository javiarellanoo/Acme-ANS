
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
		boolean status;
		int flightId;
		Flight flight;
		String flightIdStr;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			flightIdStr = super.getRequest().getData("flight", String.class);
			try {
				flightId = Integer.parseInt(flightIdStr);
				flight = this.repository.findFlightById(flightId);
				if (flightId == 0)
					status = true;
				else
					status = flight != null && !flight.getDraftMode() && flight.getScheduledDeparture() != null && MomentHelper.isAfterOrEqual(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment());
			} catch (NumberFormatException e) {
				status = false;
			}

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

		Collection<Flight> futureFlights = flights.stream().filter(flight -> flight.getScheduledDeparture() != null && MomentHelper.isAfterOrEqual(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment())).toList();

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
