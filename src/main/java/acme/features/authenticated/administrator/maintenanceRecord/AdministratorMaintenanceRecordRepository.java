
package acme.features.authenticated.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Repository
public interface AdministratorMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.draftMode = FALSE")
	Collection<MaintenanceRecord> findMaintenanceRecordsPublished();
}
