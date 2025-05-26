
package acme.features.authenticated.administrator.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select COALESCE(1.0 * count(a) / NULLIF((select count(r) from Review r),0),0.0) from Review a where a.score > 5")
	Double findReviewRatio();

	@Query("select COALESCE(1.0 * count(a) / NULLIF((select count(r) from Airline r),0),0.0) from Airline a where (a.phoneNumber != null and a.email != null)")
	Double findAirlineRatio();

	@Query("select COALESCE(1.0 * count(a) / NULLIF((select count(r) from Aircraft r),0),0.0) from Aircraft a where a.status = 0")
	Double findActiveAircraftsRatio();

	@Query("select a.operationalScope, count(a) from Airport a  group by a.operationalScope")
	public List<Object[]> findAirportsGroupedByScope();

	@Query("select a.type, count(a) from Airline a  group by a.type")
	public List<Object[]> findAirlinesGroupedByType();

	@Query("select count(r) from Review r where r.postedAt between :start and :end")
	Long numberOfReviewsPostedInWeek(Date start, Date end);

}
