
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :id AND t.trackIndex < :index ORDER BY t.trackIndex ASC")
	List<TrackingLog> findAllOrderedByIndex(Integer id, Integer index);

}
