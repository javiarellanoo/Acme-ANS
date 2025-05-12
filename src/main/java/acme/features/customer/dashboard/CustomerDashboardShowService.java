
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

		Collection<Flight> lastFiveDestinations = this.repository.getFlightsOrderByRecentBooking(customerId).stream().limit(5).toList();
		Collection<Booking> bookings = this.repository.findBookings(customerId);

		Date lastYearMoment = MomentHelper.getCurrentMoment();
		ZonedDateTime zonedDateTime = lastYearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime = zonedDateTime.minusYears(1);
		Date dateLastYear = Date.from(newZonedDateTime.toInstant());

		Map<String, Double> moneySpentLastYearByCurrency = bookings.stream().filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear))
			.filter(x -> x.getPrice() != null && x.getPrice().getAmount() != null && x.getPrice().getCurrency() != null).collect(Collectors.groupingBy(x -> x.getPrice().getCurrency(), Collectors.summingDouble(x -> x.getPrice().getAmount())));

		Collection<String> currencies = bookings.stream().map(booking -> booking.getPrice().getCurrency()).filter(c -> c != null && !c.isEmpty()).distinct().sorted().collect(Collectors.toList());

		Collection<Object[]> bookingsByTravelClassData = this.repository.findBookingsGroupedByTravelClass(customerId);
		Map<String, Integer> bookingsByTravelClass = bookingsByTravelClassData.stream().collect(Collectors.toMap(arr -> ((TravelClass) arr[0]).name(), arr -> ((Number) arr[1]).intValue()));

		Date last5YearMoment = MomentHelper.getCurrentMoment();
		ZonedDateTime zonedDateTime5 = last5YearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime5 = zonedDateTime5.minusYears(5);
		Date dateLastYear5 = Date.from(newZonedDateTime5.toInstant());

		List<Booking> bookingsLastFiveYears = bookings.stream().filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear5))
			.filter(x -> x.getPrice() != null && x.getPrice().getAmount() != null && x.getPrice().getCurrency() != null).collect(Collectors.toList());

		Map<String, List<Double>> amountsByCurrencyLastFiveYears = bookingsLastFiveYears.stream().collect(Collectors.groupingBy(b -> b.getPrice().getCurrency(), Collectors.mapping(b -> b.getPrice().getAmount(), Collectors.toList())));

		Map<String, Integer> countBookingCostLastFiveYearsByCurrency = new HashMap<>();
		Map<String, Double> averageBookingCostLastFiveYearsByCurrency = new HashMap<>();
		Map<String, Double> minBookingCostLastFiveYearsByCurrency = new HashMap<>();
		Map<String, Double> maxBookingCostLastFiveYearsByCurrency = new HashMap<>();
		Map<String, Double> stdDevBookingCostLastFiveYearsByCurrency = new HashMap<>();

		for (String currency : currencies) {
			List<Double> amountsInCurrency = amountsByCurrencyLastFiveYears.getOrDefault(currency, Collections.emptyList());
			if (!amountsInCurrency.isEmpty()) {
				countBookingCostLastFiveYearsByCurrency.put(currency, amountsInCurrency.size());
				averageBookingCostLastFiveYearsByCurrency.put(currency, this.calculateAverage(amountsInCurrency));
				minBookingCostLastFiveYearsByCurrency.put(currency, this.calculateMinimum(amountsInCurrency));
				maxBookingCostLastFiveYearsByCurrency.put(currency, this.calculateMaximum(amountsInCurrency));
				stdDevBookingCostLastFiveYearsByCurrency.put(currency, this.calculateStdDev(amountsInCurrency));
			} else {
				countBookingCostLastFiveYearsByCurrency.put(currency, 0);
				averageBookingCostLastFiveYearsByCurrency.put(currency, 0.0);
				minBookingCostLastFiveYearsByCurrency.put(currency, 0.0);
				maxBookingCostLastFiveYearsByCurrency.put(currency, 0.0);
				stdDevBookingCostLastFiveYearsByCurrency.put(currency, 0.0);
			}
		}

		Collection<Long> passengerCountsPerBooking = this.repository.findBookingRecordCountsPerBooking(customerId);
		Integer bookingCountWithPassengers = passengerCountsPerBooking.size();
		OptionalDouble avgPassengersOpt = passengerCountsPerBooking.stream().mapToLong(Long::longValue).average();
		Double avgPassengers = avgPassengersOpt.isPresent() ? avgPassengersOpt.getAsDouble() : null;
		Long minPassengers = passengerCountsPerBooking.stream().mapToLong(Long::longValue).min().orElse(-1L);
		Long maxPassengers = passengerCountsPerBooking.stream().mapToLong(Long::longValue).max().orElse(-1L);
		Double stddevPassengers = this.calculateLongStdDev(passengerCountsPerBooking);

		String[] destinations = lastFiveDestinations.stream().map(x -> x.getDestinationCity()).toArray(String[]::new);
		CustomerDashboard dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(destinations);
		dashboard.setMoneySpentLastYearByCurrency(moneySpentLastYearByCurrency);
		dashboard.setTravelClassGrouped(bookingsByTravelClass);
		dashboard.setAverageBookingCostLastFiveYearsByCurrency(averageBookingCostLastFiveYearsByCurrency);
		dashboard.setMinBookingCostLastFiveYearsByCurrency(minBookingCostLastFiveYearsByCurrency);
		dashboard.setMaxBookingCostLastFiveYearsByCurrency(maxBookingCostLastFiveYearsByCurrency);
		dashboard.setCountBookingCostLastFiveYearsByCurrency(countBookingCostLastFiveYearsByCurrency);
		dashboard.setStdDevBookingCostLastFiveYearsByCurrency(stdDevBookingCostLastFiveYearsByCurrency);
		dashboard.setCountPassengers(bookingCountWithPassengers != null ? bookingCountWithPassengers : 0);
		dashboard.setAveragePassengers(avgPassengers != null ? avgPassengers : 0);
		dashboard.setMinPassengers(minPassengers != -1L ? minPassengers.intValue() : 0);
		dashboard.setMaxPassengers(maxPassengers != -1L ? maxPassengers.intValue() : 0);
		dashboard.setStdDevPassengers(stddevPassengers != null ? stddevPassengers : 0);
		dashboard.setCurrencies(currencies);
		if (!currencies.isEmpty())
			dashboard.setSelectedCurrency(currencies.iterator().next());

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYearByCurrency", "travelClassGrouped", "averageBookingCostLastFiveYearsByCurrency", "minBookingCostLastFiveYearsByCurrency",
			"maxBookingCostLastFiveYearsByCurrency", "countBookingCostLastFiveYearsByCurrency", "stdDevBookingCostLastFiveYearsByCurrency", "countPassengers", "averagePassengers", "minPassengers", "maxPassengers", "stdDevPassengers", "selectedCurrency",
			"currencies");

		super.getResponse().addData(dataset);
	}

	private Double calculateAverage(final Collection<Double> numbers) {
		if (numbers == null || numbers.isEmpty())
			return 0.0;
		return numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
	}

	private Double calculateMinimum(final Collection<Double> numbers) {
		if (numbers == null || numbers.isEmpty())
			return 0.0;
		return numbers.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
	}

	private Double calculateMaximum(final Collection<Double> numbers) {
		if (numbers == null || numbers.isEmpty())
			return 0.0;
		return numbers.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
	}

	private Double calculateStdDev(final Collection<Double> numbers) {
		if (numbers == null || numbers.size() < 2)
			return 0.0;
		double average = this.calculateAverage(numbers);
		double sumOfSquaredDifferences = numbers.stream().mapToDouble(num -> Math.pow(num - average, 2)).sum();
		return Math.sqrt(sumOfSquaredDifferences / numbers.size());
	}

	private Double calculateLongStdDev(final Collection<Long> numbers) {
		if (numbers == null || numbers.isEmpty())
			return null;

		double avg = numbers.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
		if (Double.isNaN(avg))
			return null;

		double sumSquaredDiffs = numbers.stream().mapToDouble(num -> Math.pow(num - avg, 2)).sum();
		double variance = !numbers.isEmpty() ? sumSquaredDiffs / numbers.size() : 0.0;
		return Math.sqrt(variance);
	}
}
