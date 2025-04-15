
package acme.features.authenticated.administrator.airlines;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;

public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id =:id")
	Airline findAirlineById(int id);

}
