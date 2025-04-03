
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c FROM Claim c WHERE c.registeredBy = :assitanceId")
	List<Claim> findClaimsByAssistanceAgent(int assistanceId);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :id")
	Collection<TrackingLog> findTrackingLogsByClaim(int id);

}
