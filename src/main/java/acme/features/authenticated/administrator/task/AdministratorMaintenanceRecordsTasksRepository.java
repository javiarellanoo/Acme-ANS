
package acme.features.authenticated.administrator.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;

@Repository
public interface AdministratorMaintenanceRecordsTasksRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select mrt.task from MaintenanceRecordsTasks mrt where mrt.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select distinct mrt.task from MaintenanceRecordsTasks mrt where (mrt.task.id = :id and mrt.maintenanceRecord.draftMode = false)")
	Task findValidTaskById(int id);
}
