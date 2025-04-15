
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.draftMode = FALSE")
	Collection<Task> findTasksPublished();

	@Query("select mrt from MaintenanceRecordsTasks mrt where mrt.task.id = :id")
	Collection<MaintenanceRecordsTasks> findRelationshipsByTask(int id);

	@Query("select mrt.task from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);
}
