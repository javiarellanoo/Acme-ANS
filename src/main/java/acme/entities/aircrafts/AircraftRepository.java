
package acme.entities.aircrafts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("select count(a) from Aircraft a where registrationNumber = :registrationNumber")
	Long countSameRegistrationNumber(String registrationNumber);

}
