
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id =: flightId")
	Collection<Leg> findAllLegsByFlightId(Integer flightId);

	@Query("select f from Flight f where f.id =: id")
	Flight findFlightById(Integer id);

}
