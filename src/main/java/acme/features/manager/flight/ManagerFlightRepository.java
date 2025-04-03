
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(Integer id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findFlightsByManagerId(Integer managerId);

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(Integer id);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(Integer flightId);
}
