
package acme.entities.airlines;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("select count(a) from Airline a where a.iataCode = :iataCode")
	Integer countSameIataCode(String iataCode);

	@Query("select a from Airline a where a.iataCode = :iataCode")
	Airline findAirlineByIataCode(String iataCode);

}
