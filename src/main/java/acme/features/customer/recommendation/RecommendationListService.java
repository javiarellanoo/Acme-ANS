
package acme.features.customer.recommendation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.recommendations.Recommendation;
import acme.features.customer.dashboard.CustomerDashboardRepository; // Need this for flights
import acme.realms.Customer;

@GuiService
public class RecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private RecommendationService		recommendationApiService; // Service that calls Geoapify

	@Autowired
	private CustomerDashboardRepository	dashboardRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> lastFiveFlights = this.dashboardRepository.getFlightsOrderByRecentBooking(customerId);
		String latestDestinationCity = null;
		if (lastFiveFlights != null && !lastFiveFlights.isEmpty()) {
			Flight latestFlight = lastFiveFlights.iterator().next(); // First element is the most recent
			latestDestinationCity = latestFlight.getDestinationCity();
		}
		List<Recommendation> recommendations = this.recommendationApiService.getRecommendations(latestDestinationCity);
		super.getBuffer().addData("recommendationsList", recommendations);
		super.getBuffer().addData("cityName", latestDestinationCity != null ? latestDestinationCity : "(Unknown)");
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		@SuppressWarnings("unchecked")
		List<Recommendation> recommendations = (List<Recommendation>) super.getBuffer().getData().get("recommendationsList");
		String cityName = (String) super.getBuffer().getData().get("cityName");
		super.getResponse().addData("recommendations", recommendations);
		super.getResponse().addData("cityName", cityName);
	}

}
