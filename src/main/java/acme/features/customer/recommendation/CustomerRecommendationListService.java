
package acme.features.customer.recommendation;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.recommendation.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	private CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsByCustomerId(customerId);
		Collection<Recommendation> allRecommendations = this.repository.findRecommendations();

		Set<String> cityCountryPairs = bookings.stream().map(b -> b.getFlight().getDestinationCity() + "::" + b.getFlight().getDestinationCountry()).collect(Collectors.toSet());

		Collection<Recommendation> filtered = allRecommendations.stream().filter(r -> cityCountryPairs.contains(r.getCity() + "::" + r.getCountry())).collect(Collectors.toList());

		super.getBuffer().addData(filtered);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "city", "state", "country", "formatted", "openingHours", "url");
		super.addPayload(dataset, recommendation, "id");
		super.getResponse().addData(dataset);
	}
}
