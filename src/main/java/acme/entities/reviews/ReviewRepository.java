
package acme.entities.reviews;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ReviewRepository extends AbstractRepository {

	@Query("SELECT r FROM Review t WHERE r.employeeCode = :employeeCode")
	List<Review> findSameEmployeeCode(String employeeCode);
}
