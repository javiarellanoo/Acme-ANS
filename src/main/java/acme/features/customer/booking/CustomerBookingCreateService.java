
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Random;

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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;
		Random random = new Random();

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		StringBuilder stringBuilder = new StringBuilder();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		int length = random.nextInt(3) + 6;

		for (int i = 0; i < length; i++)
			stringBuilder.append(chars.charAt(random.nextInt(chars.length())));

		booking = new Booking();
		booking.setLocatorCode(stringBuilder.toString());
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setCustomer(customer);

		StringBuilder stringBuilderCard = new StringBuilder();

		for (int i = 0; i < 4; i++)
			stringBuilderCard.append(random.nextInt(10));

		booking.setLastCardNibble(stringBuilderCard.toString());

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble");
	}

	@Override
	public void validate(final Booking booking) {
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices choices;
		SelectChoices flightChoices;

		Dataset dataset;

		Collection<Flight> flights = this.repository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "description", booking.getFlight());

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastCardNibble", "draftMode");
		dataset.put("travelClass", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}
}
