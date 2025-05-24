
package acme.features.assistanceAgent.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;

@Repository
public interface AssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :agentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int agentId);

	@Query("select coalesce(1.0 * count(c) / nullif((select count(c2) from Claim c2 where c2.assistanceAgent.id = :agentId), 0), 0.0) from Claim c where c.assistanceAgent.id = :agentId and c.draftMode = false")
	Double ratioClaimsSuccesfullyResolvedByAgent(int agentId);

	@Query("select function('year', c.registrationMoment), function('month', c.registrationMoment), count(c) from Claim c group by function('year', c.registrationMoment), function('month', c.registrationMoment) order by count(c) desc")
	List<Object[]> top3MonthsWithMoreClaims(PageRequest pageRequest);

	@Query("select 1.0 * (select count(t) from TrackingLog t where t.claim.id = c.id) from Claim c where c.assistanceAgent.id = :agentId")
	List<Double> findNumberTLogsByClaimOfAgent(int agentId);
}
