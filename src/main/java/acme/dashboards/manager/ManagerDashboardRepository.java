
package acme.dashboards.manager;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select 65 - (YEAR(CURRENT_DATE) - YEAR(m.dateOfBirth)) from Manager m where m.userAccount.id = :managerId")
	public Integer findYearsToRetire(Integer managerId);

	@Query("select count(m2) +1 from Manager m2 where m2.yearsOfExperience >= all(select m from Manager m where m.userAccount.id =:managerId)")
	public Integer findPositionInRanking(Integer managerId);

	@Query("select 1.0 * count(a) / (select count(l) from Leg l where l.flight.manager.userAccount.id = :managerId) from Leg a where a.status = acme.entities.legs.LegStatus.ON_TIME and a.flight.manager.userAccount.id = :managerId")
	public Double findOnTimeRatio(Integer managerId);

	@Query("select 1.0 * count(a) / (select count(l) from Leg l where l.flight.manager.userAccount.id = :managerId) from Leg a where a.status = acme.entities.legs.LegStatus.DELAYED and a.flight.manager.userAccount.id = :managerId")
	public Double findDelayedRatio(Integer managerId);

	@Query("select a.name,count(l) as flightCount from Leg l join l.departureAirport a where l.departureAirport = a or l.destinationAirport = a and l.flight.manager.userAccount.id = :managerId group by a order by flightCount asc")
	public Map<String, Integer> findLeastPopularAirports(Integer managerId);

	@Query("select a.name,count(l) as flightCount from Leg l join l.departureAirport a where l.departureAirport = a or l.destinationAirport = a and l.flight.manager.userAccount.id = :managerId group by a order by flightCount desc")
	public Map<String, Integer> findMostPopularAirports(Integer managerId);

	@Query("select l.status, count(l) from Leg l where l.flight.manager.userAccount.id = :managerId group by l.status")
	public Map<String, Integer> findLegsGroupedByStatus(Integer managerId);

	@Query("select avg(l.cost.amount), min(l.cost.amount), max(l.cost.amount),stddev(l.cost.amount) from Flight l where l.manager.userAccount.id = :managerId")
	public List<Double> findStatisticalMeasures(Integer managerId);

}
