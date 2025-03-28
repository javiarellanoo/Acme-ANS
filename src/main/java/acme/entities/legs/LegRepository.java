
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l.departureAirport.city from Leg l where l.flight.id = :flightId and l.scheduledDeparture <= all (select k.scheduledDeparture from Leg k where k.flight.id = :flightId)")
	String findOriginCity(Integer flightId);

	@Query("select l.destinationAirport.city from Leg l where l.flight.id = :flightId and l.scheduledArrival >= all (select k.scheduledArrival from Leg k where k.flight.id = :flightId)")
	String findDestinationCity(Integer flightId);

	@Query("select l.scheduledDeparture from Leg l where l.flight.id = :flightId and l.scheduledDeparture <= all (select k.scheduledDeparture from Leg k where k.flight.id = :flightId)")
	Date findDepartureTime(Integer flightId);

	@Query("select l.scheduledArrival from Leg l where l.flight.id = :flightId and l.scheduledArrival >= all (select l.scheduledArrival from Leg l where l.flight.id = :flightId)")
	Date findArrivalTime(Integer flightId);

	@Query("select count(l) - 1 from Leg l where l.flight.id = :flightId")
	Integer findLayovers(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	List<Leg> findAllLegsByFlightId(Integer flightId);

	@Query("select count(l) from Leg l where l.flightNumber = :flightNumber")
	Integer countSameFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(String flightNumber);
}
