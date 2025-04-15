
package acme.features.technician.maintenanceRecordsTasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianMaintenanceRecordsTasksRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Task t where t not in (select mrt.task from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :maintenanceRecordId) and (t.draftMode = false or t.technician.id = :technicianId)")
	Collection<Task> findTasks(int maintenanceRecordId, int technicianId);

	@Query("select mrt from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<MaintenanceRecordsTasks> findTasksByMaintenanceRecordId(int id);

	@Query("select mrt.task from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordIdTask(int id);

	@Query("select mrt from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :maintenanceRecordId and mrt.task.id = :taskId")
	MaintenanceRecordsTasks findByMaintenanceRecordAndTask(int maintenanceRecordId, int taskId);
}
