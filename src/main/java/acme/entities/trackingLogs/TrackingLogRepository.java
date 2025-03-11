
package acme.entities.trackingLogs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select max(t.resolutionPercentage) from TrackingLog t")
	Double getLastTrakingLogPercentage();

}
