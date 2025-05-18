package acme.features.customer.dashboard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.forms.CustomerDashboard;
import acme.forms.Statistics;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Last five destinations
		Collection<Flight> lastFiveDestinationsList = this.repository.getFlightsOrderByRecentBooking(customerId)
				.stream().limit(5).toList();
		String[] lastFiveDestinations = lastFiveDestinationsList.stream().map(f -> f.getDestinationCity())
				.toArray(String[]::new);

		// All bookings
		Collection<Booking> bookings = this.repository.findBookings(customerId);

		// Money spent last year by currency
		Date lastYearMoment = MomentHelper.getCurrentMoment();
		ZonedDateTime zonedDateTime = lastYearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime = zonedDateTime.minusYears(1);
		Date dateLastYear = Date.from(newZonedDateTime.toInstant());
		Map<String, Double> moneySpentLastYearByCurrency = bookings.stream()
				.filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear))
				.filter(x -> x.getPrice() != null && x.getPrice().getAmount() != null
						&& x.getPrice().getCurrency() != null)
				.collect(Collectors.groupingBy(x -> x.getPrice().getCurrency(),
						Collectors.summingDouble(x -> x.getPrice().getAmount())));

		// Travel class grouped
		Collection<Object[]> bookingsByTravelClassData = this.repository.findBookingsGroupedByTravelClass(customerId);
		Map<String, Integer> travelClassGrouped = bookingsByTravelClassData.stream()
				.collect(Collectors.toMap(arr -> ((TravelClass) arr[0]).name(), arr -> ((Number) arr[1]).intValue()));

		// Cost statistics last five years
		Date last5YearMoment = MomentHelper.getCurrentMoment();
		ZonedDateTime zonedDateTime5 = last5YearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime5 = zonedDateTime5.minusYears(5);
		Date dateLastYear5 = Date.from(newZonedDateTime5.toInstant());
		List<Booking> bookingsLastFiveYears = bookings.stream()
				.filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear5))
				.filter(x -> x.getPrice() != null && x.getPrice().getAmount() != null
						&& x.getPrice().getCurrency() != null)
				.collect(Collectors.toList());
		Map<String, List<Double>> amountsByCurrencyLastFiveYears = bookingsLastFiveYears.stream()
				.collect(Collectors.groupingBy(b -> b.getPrice().getCurrency(),
						Collectors.mapping(b -> b.getPrice().getAmount(), Collectors.toList())));
		Map<String, Statistics> costStatisticsLastFiveYears = new HashMap<>();
		for (Map.Entry<String, List<Double>> entry : amountsByCurrencyLastFiveYears.entrySet()) {
			Statistics stats = new Statistics();
			stats.setData(entry.getValue());
			costStatisticsLastFiveYears.put(entry.getKey(), stats);
		}

		// Passengers statistics
		Statistics passengersStatistics = new Statistics();
		Collection<Long> bookingRecordCounts = this.repository.findBookingRecordCountsPerBooking(customerId);
		List<Double> passengersCounts = bookingRecordCounts.stream()
				.map(Long::doubleValue)
				.collect(Collectors.toList());
		passengersStatistics.setData(passengersCounts);

		// Build dashboard
		CustomerDashboard dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setMoneySpentLastYearByCurrency(moneySpentLastYearByCurrency);
		dashboard.setTravelClassGrouped(travelClassGrouped);
		dashboard.setCostStatisticsLastFiveYears(costStatisticsLastFiveYears);
		dashboard.setPassengersStatistics(passengersStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYearByCurrency",
				"travelClassGrouped", "costStatisticsLastFiveYears", "passengersStatistics");

		super.getResponse().addData(dataset);
	}

}
