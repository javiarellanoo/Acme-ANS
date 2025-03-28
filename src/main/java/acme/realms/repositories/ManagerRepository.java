
package acme.realms.repositories;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.realms.Manager;

public interface ManagerRepository extends AbstractRepository {

	@Query("select count(m) from Manager m where m.identifier = :identifier")
	Integer countSameIdentifier(String identifier);

	@Query("select m from Manager m where m.identifier = :identifier")
	Manager findManagerByIdentifier(String identifier);

}
