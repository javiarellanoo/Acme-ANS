
package acme.features.customer.dashboard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
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

		Collection<Flight> lastFiveDestinations = this.repository.getFlightsOrderByRecentBooking(customerId);

		Collection<Booking> bookings = this.repository.findBookings(customerId);

		Date lastYearMoment = MomentHelper.getCurrentMoment();

		ZonedDateTime zonedDateTime = lastYearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime = zonedDateTime.minusYears(1);
		Date dateLastYear = Date.from(newZonedDateTime.toInstant());

		Double moneySpentLastYear = bookings.stream().filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear)).filter(x -> x.getPrice() != null && x.getPrice().getAmount() != null).map(x -> x.getPrice().getAmount())
			.collect(Collectors.summingDouble(Double::doubleValue));

		Collection<Object[]> bookingsByTravelClassData = this.repository.findBookingsGroupedByTravelClass(customerId);
		Map<String, Integer> bookingsByTravelClass = bookingsByTravelClassData.stream().collect(Collectors.toMap(arr -> ((TravelClass) arr[0]).name(), // Use imported TravelClass and .name()
			arr -> ((Number) arr[1]).intValue()));

		Date last5YearMoment = MomentHelper.getCurrentMoment();

		ZonedDateTime zonedDateTime5 = last5YearMoment.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime5 = zonedDateTime5.minusYears(5);
		Date dateLastYear5 = Date.from(newZonedDateTime5.toInstant());

		Collection<Money> bookingCostsLastFiveYears = this.repository.findBookings(customerId).stream().filter(x -> x.getPurchaseMoment() != null && x.getPurchaseMoment().after(dateLastYear5)).map(Booking::getPrice).toList();

		int bookingCountLastFiveYears = bookingCostsLastFiveYears.size();
		Money avgBookingCost = this.calculateAverageMoney(bookingCostsLastFiveYears);
		Money minBookingCost = this.calculateMinimumMoney(bookingCostsLastFiveYears);
		Money maxBookingCost = this.calculateMaximumMoney(bookingCostsLastFiveYears);
		Money stddevBookingCost = this.calculateStdDevMoney(bookingCostsLastFiveYears);

		Collection<Long> passengerCountsPerBooking = this.repository.findBookingRecordCountsPerBooking(customerId);
		Integer bookingCountWithPassengers = passengerCountsPerBooking.size(); // This is the count for stats
		OptionalDouble avgPassengersOpt = passengerCountsPerBooking.stream().mapToLong(Long::longValue).average();
		Double avgPassengers = avgPassengersOpt.isPresent() ? avgPassengersOpt.getAsDouble() : null;
		Long minPassengers = passengerCountsPerBooking.stream().mapToLong(Long::longValue).min().orElse(-1L); // Use -1L
																												// as
																												// sentinel
		Long maxPassengers = passengerCountsPerBooking.stream().mapToLong(Long::longValue).max().orElse(-1L); // Use -1L
																												// as
																												// sentinel
		Double stddevPassengers = this.calculateLongStdDev(passengerCountsPerBooking);

		CustomerDashboard dashboard = new CustomerDashboard();

		dashboard.setLastFiveDestinations(lastFiveDestinations.stream().map(Flight::getDestinationCity).limit(5).toArray(String[]::new));
		dashboard.setMoneySpentLastYear(moneySpentLastYear != null ? moneySpentLastYear : 0);
		dashboard.setTravelClassGrouped(bookingsByTravelClass);
		dashboard.setAverageBookingCostLastFiveYears(avgBookingCost.getAmount() != null ? avgBookingCost.getAmount() : 0);
		dashboard.setMinBookingCostLastFiveYears(minBookingCost.getAmount() != null ? minBookingCost.getAmount() : 0);
		dashboard.setMaxBookingCostLastFiveYears(maxBookingCost.getAmount() != null ? maxBookingCost.getAmount() : 0);
		dashboard.setCountBookingCostLastFiveYears(bookingCountLastFiveYears);
		dashboard.setStdDevBookingCostLastFiveYears(stddevBookingCost.getAmount() != null ? stddevBookingCost.getAmount() : 0);
		dashboard.setCountPassengers(bookingCountWithPassengers != null ? bookingCountWithPassengers : 0);
		dashboard.setAveragePassengers(avgPassengers != null ? avgPassengers : 0);
		dashboard.setMinPassengers(minPassengers != -1L ? minPassengers.intValue() : 0);
		dashboard.setMaxPassengers(maxPassengers != -1L ? maxPassengers.intValue() : 0);
		dashboard.setStdDevPassengers(stddevPassengers != null ? stddevPassengers : 0);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYear", "travelClassGrouped", "averageBookingCostLastFiveYears", "minBookingCostLastFiveYears", "maxBookingCostLastFiveYears", "countBookingCostLastFiveYears",
			"stdDevBookingCostLastFiveYears", "countPassengers", "averagePassengers", "minPassengers", "maxPassengers", "stdDevPassengers");

		super.getResponse().addData(dataset);
	}

	private Money calculateAverageMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null);
		else
			moneyFinal.setAmount(budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).average().orElse(Double.NaN)); // Keep NaN if stream is somehow empty post-filter
		return moneyFinal;
	}

	private Money calculateMaximumMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null);
		else
			moneyFinal.setAmount(budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).max().orElse(Double.NaN)); // Keep
																																	// //
																																	// empty
																																	// post-filter
		return moneyFinal;
	}

	private Money calculateMinimumMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null);
		else
			moneyFinal.setAmount(budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).min().orElse(Double.NaN)); // Keep
																																	// //
																																	// post-filter
		return moneyFinal;
	}

	private Money calculateStdDevMoney(final Collection<Money> budgets) {
		Money standardDeviation = new Money();
		standardDeviation.setCurrency("USD");

		if (budgets == null || budgets.isEmpty()) {
			standardDeviation.setAmount(null);
			return standardDeviation;
		}

		double average = budgets.stream().mapToDouble(Money::getAmount).average().orElse(Double.NaN);
		if (Double.isNaN(average)) {
			standardDeviation.setAmount(null);
			return standardDeviation;
		}
		double sumOfSquaredDifferences = budgets.stream().mapToDouble(budget -> Math.pow(budget.getAmount() - average, 2)).sum();
		double variance = !budgets.isEmpty() ? sumOfSquaredDifferences / budgets.size() : 0.0;
		double stdDev = Math.sqrt(variance);
		standardDeviation.setAmount(stdDev);

		return standardDeviation;
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
