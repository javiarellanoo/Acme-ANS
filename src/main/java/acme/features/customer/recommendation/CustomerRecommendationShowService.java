
package acme.features.customer.recommendation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.recommendation.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationShowService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	private CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recommendationId = super.getRequest().getData("id", int.class);
		Recommendation recommendation = this.repository.findRecommendationById(recommendationId);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsByCustomerId(customerId);

		if (recommendation == null)
			status = false;
		else {
			status = bookings.stream().anyMatch(b -> b.getFlight().getDestinationCity().equals(recommendation.getCity()));
			status &= bookings.stream().anyMatch(b -> b.getFlight().getDestinationCountry().equals(recommendation.getCountry()));
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Recommendation recommendation = this.repository.findRecommendationById(id);
		super.getBuffer().addData(recommendation);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "city", "state", "country", "formatted", "openingHours", "url");
		super.getResponse().addData(dataset);
	}
}
