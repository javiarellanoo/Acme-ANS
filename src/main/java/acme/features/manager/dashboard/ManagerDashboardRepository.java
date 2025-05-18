
package acme.features.manager.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select 65 - (YEAR(CURRENT_DATE) - YEAR(m.dateOfBirth)) from Manager m where m.id = :managerId")
	public Integer findYearsToRetire(Integer managerId);

	@Query("select count(m2) +1 from Manager m2 where m2.id != :managerId and m2.yearsOfExperience >= (select m.yearsOfExperience from Manager m where m.id =:managerId)")
	public Integer findPositionInRanking(Integer managerId);

	@Query("select COALESCE(1.0 * count(a) / NULLIF((select count(l) from Leg l where l.flight.manager.id = :managerId and l.flight.draftMode = false and l.draftMode = false), 0), 0.0) from Leg a where a.status = acme.entities.legs.LegStatus.ON_TIME and a.flight.manager.id = :managerId and a.flight.draftMode = false and a.draftMode = false")
	public Double findOnTimeRatio(Integer managerId);

	@Query("select COALESCE(1.0 * count(a) / NULLIF((select count(l) from Leg l where l.flight.manager.id = :managerId and l.flight.draftMode = false and l.draftMode = false), 0), 0.0) from Leg a where a.status = acme.entities.legs.LegStatus.DELAYED and a.flight.manager.id = :managerId and a.flight.draftMode = false and a.draftMode = false")
	public Double findDelayedRatio(Integer managerId);

	@Query("select a.name as flightCount from Leg l join l.departureAirport a where (l.departureAirport = a or l.destinationAirport = a) and l.flight.manager.id = :managerId and l.flight.draftMode = false and l.draftMode = false group by a order by flightCount asc")
	public String findLeastPopularAirports(Integer managerId, PageRequest pageRequest);

	@Query("select a.name as flightCount from Leg l join l.departureAirport a where (l.departureAirport = a or l.destinationAirport = a) and l.flight.manager.id = :managerId and l.flight.draftMode = false and l.draftMode = false  group by a order by flightCount desc")
	public String findMostPopularAirports(Integer managerId, PageRequest pageRequest);

	@Query("select l.status, count(l) from Leg l where l.flight.manager.id = :managerId and l.flight.draftMode = false and l.draftMode = false group by l.status")
	public List<Object[]> findLegsGroupedByStatus(Integer managerId);

	@Query("select distinct f.cost.currency from Flight f where f.manager.id = :managerId and f.draftMode = false")
	public Collection<String> findCurrencies(int managerId);

	@Query("select f.cost.amount from Flight f where f.manager.id=:managerId and f.draftMode = false and f.cost.currency = :currency")
	public List<Double> findCostsByCurrency(int managerId, String currency);
}
