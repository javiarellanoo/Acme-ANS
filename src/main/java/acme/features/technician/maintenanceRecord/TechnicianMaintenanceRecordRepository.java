
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.draftMode = FALSE")
	Collection<MaintenanceRecord> findMaintenanceRecordsPublished();

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select m from MaintenanceRecord m where m.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("select mrt from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<MaintenanceRecordsTasks> findRelationshipsByMaintenanceRecord(int id);

	@Query("select mrt.task from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAircrafts();

}
