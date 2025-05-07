
package acme.features.customer.recommendation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.recommendations.Recommendation;

@Repository
public interface CustomerRecommendationRepository extends AbstractRepository {

	@Query("select r from Recommendation r where r.city = :city")
	Collection<Recommendation> findRecommendationsByCity(String city);
}
