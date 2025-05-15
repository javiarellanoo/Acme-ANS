
package acme.features.technician.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("select count(mr) from MaintenanceRecord mr where (mr.technician.id = :technicianId and mr.status = :status) group by mr.status")
	Integer mapStatusNumberRecords(int technicianId, MaintenanceRecordStatus status);

	@Query("select distinct mrt.maintenanceRecord from MaintenanceRecordsTasks mrt where (mrt.maintenanceRecord.nextInspectionDate > :actualDate and mrt.task.technician.id = :technicianId) order by mrt.maintenanceRecord.nextInspectionDate")
	List<MaintenanceRecord> recordNearestInspectionDueDate(PageRequest pageRequest, Date actualDate, int technicianId);

	@Query("select mrt.maintenanceRecord.aircraft as aircraft, count(*) as tasks_count from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.technician.id = :technicianId group by aircraft order by tasks_count DESC")
	List<Aircraft> topAircraftsNumberRecords(PageRequest pageRequest, int technicianId);

	@Query("select distinct m.estimatedCost.currency from MaintenanceRecord m where (m.technician.id = :technicianId and year(m.moment) = year(:dateLastYear))")
	List<String> findCurrencies(int technicianId, Date dateLastYear);

	@Query("select m.estimatedCost.amount from MaintenanceRecord m where (year(m.moment) = year(:dateLastYear) and m.estimatedCost.currency = :currency and m.technician.id = :technicianId)")
	List<Double> findCostsByCurrencyAndYear(String currency, Date dateLastYear, int technicianId);

	@Query("select t.estimatedHoursDuration from Task t where t.technician.id = :technicianId")
	List<Double> findTasksDuration(int technicianId);

}
