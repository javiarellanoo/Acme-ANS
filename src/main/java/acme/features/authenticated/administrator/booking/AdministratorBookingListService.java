
package acme.features.authenticated.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;

@GuiService
public class AdministratorBookingListService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings = this.repository.findPublishedBookings();
		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "price", "travelClass", "flight.displayString", "customer.identifier");
		dataset.put("flight", booking.getFlight().getDisplayString());
		super.addPayload(dataset, booking, "travelClass", "purchaseMoment", "lastCardNibble");
		super.getResponse().addData(dataset);
	}
}
