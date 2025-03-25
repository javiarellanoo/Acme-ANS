
package acme.realms.repositories;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface ManagerRepository extends AbstractRepository {

	@Query("select count(m) from Manager m where m.identifier = :identifier")
	public Integer countSameIdentifier(String identifier);

}
