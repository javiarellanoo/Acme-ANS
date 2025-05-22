
package acme.features.authenticated.administrator.recommendation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.recommendation.Recommendation;

@Repository
public interface AdministratorRecommendationRepository extends AbstractRepository {

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findDistinctCities();

	@Query("select r from Recommendation r")
	Collection<Recommendation> findAllRecommendations();
}
