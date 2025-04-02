
package acme.features.any.review;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.reviews.Review;

@Repository
public interface ReviewRepository extends AbstractRepository {

	@Query("SELECT r FROM Review r WHERE r.postedAt > :date")
	List<Review> findReviewsAfterDate(Date date);

	@Query("SELECT r FROM Review r WHERE r.id = :id")
	Review findReviewById(int id);

}
