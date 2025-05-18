
package acme.features.manager.dashboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.ManagerDashboard;
import acme.forms.Statistics;
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

		Map<String, Statistics> mapStatisticsCost = new HashMap<>();
		Collection<String> usedCurrencies = this.repository.findCurrencies(managerId);
		for (String currency : usedCurrencies) {
			Statistics costStatistics = new Statistics();
			List<Double> moneyAmount = this.repository.findCostsByCurrency(managerId, currency);
			costStatistics.setData(moneyAmount);
			mapStatisticsCost.put(currency, costStatistics);
		}

		ManagerDashboard dashboard = new ManagerDashboard();
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setManagerRanking(positionInRanking);

		dashboard.setDelayedRatio(delayedRatio);
		dashboard.setOnTimeRatio(inTimeRatio);
		dashboard.setLessPopularAirport(leastPopularAirport);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setNumberOfLegsPerStatus(legsByStatus);
		dashboard.setPriceStatistics(mapStatisticsCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "yearsToRetire", "managerRanking", "priceStatistics", "delayedRatio", "onTimeRatio", "lessPopularAirport", "mostPopularAirport", "numberOfLegsPerStatus");

		super.getResponse().addData(dataset);
	}

}
