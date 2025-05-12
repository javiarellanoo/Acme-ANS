
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceId")
	Collection<Claim> findClaimsByAssistanceAgent(int assistanceId);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.draftMode = false and l.flight.draftMode = false")
	Collection<Leg> findAllLegsPublishedForFlightsPublished();

	@Query("select t from TrackingLog t where t.claim.id = :id")
	Collection<TrackingLog> findTrackingLogsByClaim(int id);

	@Query("select a from AssistanceAgent a where a.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

}
