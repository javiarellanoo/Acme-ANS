
package acme.entities.airlines;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("select count(a) from Airline a where a.iataCode = :iataCode")
	public Integer countSameIataCode(String iataCode);

}
