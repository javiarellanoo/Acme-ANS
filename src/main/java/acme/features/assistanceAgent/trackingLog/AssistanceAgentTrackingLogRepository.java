
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.draftMode = false")
	Collection<TrackingLog> findTrackingLogsPublished();

	@Query("select t from TrackingLog t where t.claim.assistanceAgent.id = :id")
	Collection<TrackingLog> findTrackingLogsByAssistanceAgent(int id);

	@Query("select t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("select a from AssistanceAgent a where a.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id = :id")
	Collection<Claim> findClaimsByAssistanceAgentId(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

}
