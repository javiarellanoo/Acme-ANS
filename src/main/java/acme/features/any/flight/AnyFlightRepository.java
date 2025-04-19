
package acme.features.any.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;

@Repository
public interface AnyFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findAllPublishedFlights();

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(Integer id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

}
