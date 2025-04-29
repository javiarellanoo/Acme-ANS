
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AnyLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(Integer flightId);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(Integer id);

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(Integer id);

	@Query("select a from Aircraft a where a.airline.id =:airlineId")
	Collection<Aircraft> findAircraftsByAirlineId(Integer airlineId);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(Integer id);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(Integer id);
}
