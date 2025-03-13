
package acme.dashboards.technician;

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

	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<MaintenanceRecordStatus, Integer>	mapStatusNumberRecords;
	MaintenanceRecord						recordNearestInspectionDueDate;
	List<Aircraft>							top5AircraftsNumberRecords;
	Double									avgCostMaintenanceLastYear;
	Double									maxCostMaintenanceLastYear;
	Double									minCostMaintenanceLastYear;
	Double									sdCostMaintenanceLastYear;
	Double									avgDuration;
	Double									maxDuration;
	Double									minDuration;
	Double									sdDuration;

}
