
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long						serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<MaintenanceRecordStatus, Integer>	mapStatusNumberRecords;
	private MaintenanceRecord						recordNearestInspectionDueDate;
	private List<Aircraft>							topAircraftsNumberRecords;
	private Map<String, Statistics>					costStatisticsLastYear;
	private Statistics								durationStatistics;

}
