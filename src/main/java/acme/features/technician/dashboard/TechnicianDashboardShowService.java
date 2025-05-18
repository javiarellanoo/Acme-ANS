
package acme.features.technician.dashboard;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.forms.Statistics;
import acme.forms.TechnicianDashboard;
import acme.realms.Technician;

@GuiService
public class TechnicianDashboardShowService extends AbstractGuiService<Technician, TechnicianDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TechnicianDashboard dashboard;
		int technicianId;
		PageRequest pageAircrafts;
		PageRequest pageMaintenanceRecords;
		Date actualDate;
		Map<MaintenanceRecordStatus, Integer> mapStatusNumberRecords;
		List<Aircraft> topAircraftsNumberRecords;
		MaintenanceRecord recordNearestInspectionDueDate;
		Statistics costStatistics;
		List<String> usedCurrencies;
		Map<String, Statistics> mapStatisticsCost;
		List<Double> moneyAmount;
		Statistics taskDurationStatistics;
		List<Double> tasksDurations;
		Integer countByStatus;
		List<MaintenanceRecord> recordNextsInspections;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		pageAircrafts = PageRequest.of(0, 5);
		pageMaintenanceRecords = PageRequest.of(0, 1);
		actualDate = MomentHelper.getCurrentMoment();
		ZonedDateTime zonedDateTime = actualDate.toInstant().atZone(ZoneId.systemDefault());
		ZonedDateTime newZonedDateTime = zonedDateTime.minusYears(1);
		Date dateLastYear = Date.from(newZonedDateTime.toInstant());

		mapStatisticsCost = new HashMap<>();
		usedCurrencies = this.repository.findCurrencies(technicianId, dateLastYear);
		for (String currency : usedCurrencies) {
			costStatistics = new Statistics();
			moneyAmount = this.repository.findCostsByCurrencyAndYear(currency, dateLastYear, technicianId);
			costStatistics.setData(moneyAmount);
			mapStatisticsCost.put(currency, costStatistics);
		}

		taskDurationStatistics = new Statistics();
		tasksDurations = this.repository.findTasksDuration(technicianId);
		taskDurationStatistics.setData(tasksDurations);

		mapStatusNumberRecords = new HashMap<>();
		for (MaintenanceRecordStatus status : MaintenanceRecordStatus.values()) {
			countByStatus = this.repository.mapStatusNumberRecords(technicianId, status);
			mapStatusNumberRecords.put(status, countByStatus);
		}

		topAircraftsNumberRecords = this.repository.topAircraftsNumberRecords(pageAircrafts, technicianId);

		recordNextsInspections = this.repository.recordNearestInspectionDueDate(pageMaintenanceRecords, actualDate, technicianId);
		recordNearestInspectionDueDate = recordNextsInspections.isEmpty() ? null : recordNextsInspections.get(0);

		dashboard = new TechnicianDashboard();
		dashboard.setMapStatusNumberRecords(mapStatusNumberRecords);
		dashboard.setTopAircraftsNumberRecords(topAircraftsNumberRecords);
		dashboard.setRecordNearestInspectionDueDate(recordNearestInspectionDueDate);
		dashboard.setCostStatisticsLastYear(mapStatisticsCost);
		dashboard.setDurationStatistics(taskDurationStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final TechnicianDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "mapStatusNumberRecords", "recordNearestInspectionDueDate", "topAircraftsNumberRecords", "costStatisticsLastYear", "durationStatistics");

		super.getResponse().addData(dataset);
	}
}
