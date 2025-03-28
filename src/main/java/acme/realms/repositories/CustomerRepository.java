
package acme.realms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.identifier = :identifier")
	Customer getCustomerByIdentifier(String identifier);

	@Query("select count(c) from Customer c where identifier = :identifier")
	Long countSameIdentifier(String identifier);

}
