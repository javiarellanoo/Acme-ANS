
package acme.features.authenticated.administrator.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.AdministratorDashboard;

@GuiService
public class AdministratorDashboardShowService extends AbstractGuiService<Administrator, AdministratorDashboard> {

	@Autowired
	private AdministratorDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Double activeRatio = this.repository.findActiveAircraftsRatio();
		Double inactiveRatio = 1.0 - activeRatio;
		Double reviewRatio = this.repository.findReviewRatio();
		Double airlineRatio = this.repository.findAirlineRatio();

		List<Object[]> rawAirports = this.repository.findAirportsGroupedByScope();
		Map<String, Integer> airportsByScope = new HashMap<>();
		for (Object[] row : rawAirports) {
			String status = row[0].toString();
			Long count = (Long) row[1];
			Integer countvalue = count.intValue();
			airportsByScope.put(status, countvalue);
		}

		List<Object[]> rawAirlines = this.repository.findAirlinesGroupedByType();
		Map<String, Integer> airlinesByType = new HashMap<>();
		for (Object[] row : rawAirlines) {
			String status = row[0].toString();
			Long count = (Long) row[1];
			Integer countvalue = count.intValue();
			airlinesByType.put(status, countvalue);
		}

		List<Long> weeklyNumberOfReviews = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Long numReviews;
			Date start, end;

			numReviews = 0L;
			start = MomentHelper.deltaFromCurrentMoment(-i, ChronoUnit.WEEKS);
			end = MomentHelper.deltaFromCurrentMoment(-i + 1L, ChronoUnit.WEEKS);

			numReviews = this.repository.numberOfReviewsPostedInWeek(start, end);
			weeklyNumberOfReviews.add(numReviews);
		}
		Long numberOfReviews = weeklyNumberOfReviews.stream().mapToLong(x -> x).sum();
		Long minimumNumber = weeklyNumberOfReviews.stream().mapToLong(x -> x).min().orElse(0L);
		Long maximumNumber = weeklyNumberOfReviews.stream().mapToLong(x -> x).max().orElse(0L);
		Double averageNumber = weeklyNumberOfReviews.stream().mapToLong(x -> x).average().orElse(0L);
		Double variance = weeklyNumberOfReviews.stream().mapToDouble(x -> Math.pow(x.doubleValue() - averageNumber, 2.0)).sum() / (weeklyNumberOfReviews.size() - 1);
		Double stdDev = Math.sqrt(variance);

		AdministratorDashboard dashboard = new AdministratorDashboard();
		dashboard.setActiveAircraftsRatio(activeRatio);
		dashboard.setNonActiveAircraftsRatio(inactiveRatio);
		dashboard.setAirlinesWithMailAndPhoneRatio(airlineRatio);
		dashboard.setReviewRatio(reviewRatio);
		dashboard.setNumberOfAirlinesPerType(airlinesByType);
		dashboard.setNumberOfAirportsPerScope(airportsByScope);
		dashboard.setAverageNumberOfReviews(averageNumber);
		dashboard.setMaximumNumberOfReviews(maximumNumber);
		dashboard.setMinimumNumberOfReviews(minimumNumber);
		dashboard.setNumberOfReviews(numberOfReviews);
		dashboard.setStdDevReviews(stdDev);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "reviewRatio", "airlinesWithMailAndPhoneRatio", "activeAircraftsRatio", "nonActiveAircraftsRatio", "numberOfAirlinesPerType", "numberOfAirportsPerScope", "averageNumberOfReviews",
			"minimumNumberOfReviews", "maximumNumberOfReviews", "numberOfReviews", "stdDevReviews");

		super.getResponse().addData(dataset);
	}

}
