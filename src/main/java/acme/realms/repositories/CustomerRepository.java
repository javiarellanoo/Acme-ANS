
package acme.realms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("select count(c) from Customer c where identifier = :identifier")
	Long countSameIdentifier(String identifier);

}
