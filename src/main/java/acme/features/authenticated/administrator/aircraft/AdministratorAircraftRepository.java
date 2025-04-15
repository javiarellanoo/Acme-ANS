
package acme.features.authenticated.administrator.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;

public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id =:id")
	Aircraft findAircraftById(int id);

}
