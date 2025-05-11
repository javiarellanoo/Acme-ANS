
package acme.features.manager.dashboard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Integer yearsToRetire = this.repository.findYearsToRetire(managerId);
		Integer positionInRanking = this.repository.findPositionInRanking(managerId);
		Double inTimeRatio = this.repository.findOnTimeRatio(managerId);
		Double delayedRatio = this.repository.findDelayedRatio(managerId);

		String mostPopularAirport = this.repository.findMostPopularAirports(managerId, PageRequest.of(0, 1));
		String leastPopularAirport = this.repository.findLeastPopularAirports(managerId, PageRequest.of(0, 1));

		List<Object[]> rawLegsByStatus = this.repository.findLegsGroupedByStatus(managerId);
		Map<String, Integer> legsByStatus = new HashMap<>();
		for (Object[] row : rawLegsByStatus) {
			String status = row[0].toString();
			Long count = (Long) row[1];
			Integer countvalue = count.intValue();
			legsByStatus.put(status, countvalue);
		}
		Collection<Flight> flights = this.repository.findAllFlights(managerId);
		Collection<String> currencies = flights.stream().map(flight -> flight.getCost().getCurrency()).filter(c -> c != null && !c.isEmpty()).distinct().sorted().collect(Collectors.toList());

		Map<String, List<Double>> amountsByCurrencies = flights.stream().collect(Collectors.groupingBy(b -> b.getCost().getCurrency(), Collectors.mapping(b -> b.getCost().getAmount(), Collectors.toList())));
		Map<String, Double> averageFlightCostByCurrency = new HashMap<>();
		Map<String, Double> maximumFlightCostByCurrency = new HashMap<>();
		Map<String, Double> minimumFlightCostByCurrency = new HashMap<>();
		Map<String, Double> stdDevFlightCostByCurrency = new HashMap<>();

		for (String currency : currencies) {
			List<Double> amountsInCurrency = amountsByCurrencies.getOrDefault(currency, Collections.emptyList());
			if (!amountsInCurrency.isEmpty()) {
				averageFlightCostByCurrency.put(currency, this.calculateAverage(amountsInCurrency));
				maximumFlightCostByCurrency.put(currency, this.calculateMinimum(amountsInCurrency));
				minimumFlightCostByCurrency.put(currency, this.calculateMaximum(amountsInCurrency));
				stdDevFlightCostByCurrency.put(currency, this.calculateStdDev(amountsInCurrency));
			} else {
				averageFlightCostByCurrency.put(currency, 0.0);
				maximumFlightCostByCurrency.put(currency, 0.0);
				minimumFlightCostByCurrency.put(currency, 0.0);
				stdDevFlightCostByCurrency.put(currency, 0.0);
			}
		}

		ManagerDashboard dashboard = new ManagerDashboard();
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setManagerRanking(positionInRanking);
		dashboard.setStdDevCostOfFlightsPerCurrency(stdDevFlightCostByCurrency);
		dashboard.setMaximumCostOfFlightsPerCurrency(maximumFlightCostByCurrency);
		dashboard.setAverageCostOfFlightsPerCurrency(averageFlightCostByCurrency);
		dashboard.setMinimumCostOfFlightsPerCurrency(minimumFlightCostByCurrency);
		dashboard.setDelayedRatio(delayedRatio);
		dashboard.setOnTimeRatio(inTimeRatio);
		dashboard.setLessPopularAirport(leastPopularAirport);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setNumberOfLegsPerStatus(legsByStatus);
		dashboard.setCurrencies(currencies);
		if (!currencies.isEmpty())
			dashboard.setSelectedCurrency(currencies.iterator().next());

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "yearsToRetire", "managerRanking", "averageCostOfFlightsPerCurrency", "maximumCostOfFlightsPerCurrency", "minimumCostOfFlightsPerCurrency", "stdDevCostOfFlightsPerCurrency", "delayedRatio",
			"onTimeRatio", "lessPopularAirport", "mostPopularAirport", "numberOfLegsPerStatus", "currencies", "selectedCurrency");

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
