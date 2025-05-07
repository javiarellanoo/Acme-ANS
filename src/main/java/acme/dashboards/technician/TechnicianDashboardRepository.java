
package acme.dashboards.technician;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("select mr.status, count(mr) from MaintenanceRecord mr where (mr.technician.id = ?1) group by mr.status")
	Map<MaintenanceRecordStatus, Integer> mapStatusNumberRecords(int technicianId);

	//@Query("")
	//MaintenanceRecord recordNearestInspectionDueDate();

	@Query("select mrt.maintenanceRecord.aircraft as aircraft, count(*) as tasks_count from MaintenanceRecordsTasks mrt group by aircraft order by tasks_count DESC")
	List<Aircraft> topAircraftsNumberRecords();

}
