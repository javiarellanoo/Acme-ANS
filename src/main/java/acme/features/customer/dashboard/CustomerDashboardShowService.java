package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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

		// Last five destinations
		Collection<Flight> lastFiveDestinations = this.repository.getFlightsOrderByRecentBooking(customerId);

		// Money spent in bookings during the last year
		Double moneySpentLastYear = this.repository.findMoneySpentLastYear(customerId);

		// Number of bookings grouped by travel class
		Collection<Object[]> bookingsByTravelClassData = this.repository.findBookingsGroupedByTravelClass(customerId);
		// Convert Collection<Object[]> to Map<String, Integer>
		Map<String, Integer> bookingsByTravelClass = bookingsByTravelClassData.stream()
				.collect(Collectors.toMap(
						arr -> ((TravelClass) arr[0]).name(), // Use imported TravelClass and .name()
						arr -> ((Number) arr[1]).intValue() // Value: count (Integer)
				));

		// Booking costs in the last five years
		Collection<Money> bookingCostsLastFiveYears = this.repository.findBookingCostsLastFiveYears(customerId).stream()
				.limit(5).collect(Collectors.toList());
		int bookingCountLastFiveYears = bookingCostsLastFiveYears.size();
		Money avgBookingCost = this.calculateAverageMoney(bookingCostsLastFiveYears);
		Money minBookingCost = this.calculateMinimumMoney(bookingCostsLastFiveYears);
		Money maxBookingCost = this.calculateMaximumMoney(bookingCostsLastFiveYears);
		Money stddevBookingCost = this.calculateStdDevMoney(bookingCostsLastFiveYears);

		// Passengers per booking statistics
		Collection<Long> passengerCountsPerBooking = this.repository.findBookingRecordCountsPerBooking(customerId);
		int bookingCountWithPassengers = passengerCountsPerBooking.size(); // This is the count for stats
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

		dashboard.setLastFiveDestinations(
				lastFiveDestinations.stream().map(x -> x.getDestinationCity()).limit(5).toArray(String[]::new));
		dashboard.setMoneySpentLastYear(moneySpentLastYear);
		dashboard.setTravelClassGrouped(bookingsByTravelClass);
		dashboard.setAverageBookingCostLastFiveYears(
				avgBookingCost.getAmount() != null ? avgBookingCost.getAmount() : null);
		dashboard
				.setMinBookingCostLastFiveYears(minBookingCost.getAmount() != null ? minBookingCost.getAmount() : null);
		dashboard
				.setMaxBookingCostLastFiveYears(maxBookingCost.getAmount() != null ? maxBookingCost.getAmount() : null);
		dashboard.setCountBookingCostLastFiveYears(bookingCountLastFiveYears);
		dashboard.setStdDevBookingCostLastFiveYears(
				stddevBookingCost.getAmount() != null ? stddevBookingCost.getAmount() : null);
		dashboard.setCountPassengers(bookingCountWithPassengers);
		dashboard.setAveragePassengers(avgPassengers);
		dashboard.setMinPassengers(minPassengers != -1L ? minPassengers.intValue() : null);
		dashboard.setMaxPassengers(maxPassengers != -1L ? maxPassengers.intValue() : null);
		dashboard.setStdDevPassengers(stddevPassengers);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYear",
				"travelClassGrouped", "averageBookingCostLastFiveYears", "minBookingCostLastFiveYears",
				"maxBookingCostLastFiveYears", "countBookingCostLastFiveYears",
				"stdDevBookingCostLastFiveYears", "countPassengers", "averagePassengers", "minPassengers",
				"maxPassengers", "stdDevPassengers");

		super.getResponse().addData(dataset);
	}

	private Money calculateAverageMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null); // Return null instead of NaN
		else
			moneyFinal.setAmount(budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).average()
					.orElse(Double.NaN)); // Keep NaN if stream is somehow empty post-filter
		return moneyFinal;
	}

	private Money calculateMaximumMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null); // Return null instead of NaN
		else
			moneyFinal.setAmount(
					budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).max().orElse(Double.NaN)); // Keep
																														// NaN
																														// if
																														// stream
																														// is
																														// somehow
																														// empty
																														// post-filter
		return moneyFinal;
	}

	private Money calculateMinimumMoney(final Collection<Money> budgets) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		if (budgets == null || budgets.isEmpty())
			moneyFinal.setAmount(null); // Return null instead of NaN
		else
			moneyFinal.setAmount(
					budgets.stream().map(Money::getAmount).mapToDouble(Double::doubleValue).min().orElse(Double.NaN)); // Keep
																														// NaN
																														// if
																														// stream
																														// is
																														// somehow
																														// empty
																														// post-filter
		return moneyFinal;
	}

	private Money calculateStdDevMoney(final Collection<Money> budgets) {
		Money desviacion = new Money();
		desviacion.setCurrency("USD");

		if (budgets == null || budgets.isEmpty()) {
			desviacion.setAmount(null); // Return null instead of NaN
			return desviacion;
		}

		double media = budgets.stream().mapToDouble(Money::getAmount).average().orElse(Double.NaN);
		if (Double.isNaN(media)) {
			desviacion.setAmount(null); // Return null instead of NaN
			return desviacion;
		}
		double sumaDiferenciasCuadradas = budgets.stream()
				.mapToDouble(budget -> Math.pow(budget.getAmount() - media, 2)).sum();
		// Check size before division
		double varianza = !budgets.isEmpty() ? sumaDiferenciasCuadradas / budgets.size() : 0.0;
		double desviacionEstandar = Math.sqrt(varianza);
		desviacion.setAmount(desviacionEstandar);

		return desviacion;
	}

	private Double calculateLongStdDev(final Collection<Long> numbers) {
		if (numbers == null || numbers.isEmpty())
			return null; // Return null instead of NaN

		double avg = numbers.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
		if (Double.isNaN(avg))
			return null; // Return null instead of NaN

		double sumSquaredDiffs = numbers.stream().mapToDouble(num -> Math.pow(num - avg, 2)).sum();
		// Check size before division
		double variance = !numbers.isEmpty() ? sumSquaredDiffs / numbers.size() : 0.0;
		return Math.sqrt(variance);
	}
}
