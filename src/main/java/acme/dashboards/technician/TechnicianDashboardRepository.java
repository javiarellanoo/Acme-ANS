
package acme.dashboards.technician;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	//Map<MaintenanceRecordStatus, Integer> mapStatusNumberRecords();

}
