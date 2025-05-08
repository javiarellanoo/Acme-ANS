
package acme.entities.legs;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l.departureAirport.city from Leg l where l.flight.id = :flightId and l.scheduledDeparture <= all (select k.scheduledDeparture from Leg k where k.flight.id = :flightId)")
	List<String> findOriginCity(Integer flightId, PageRequest pageRequest);

	@Query("select l.destinationAirport.city from Leg l where l.flight.id = :flightId and l.scheduledArrival >= all (select k.scheduledArrival from Leg k where k.flight.id = :flightId)")
	List<String> findDestinationCity(Integer flightId, PageRequest pageRequest);

	@Query("select l.destinationAirport.country from Leg l where l.flight.id = :flightId and l.scheduledArrival >= all (select k.scheduledArrival from Leg k where k.flight.id = :flightId)")
	List<String> findDestinationCountry(Integer flightId, PageRequest pageRequest);

	@Query("select l.scheduledDeparture from Leg l where l.flight.id = :flightId and l.scheduledDeparture <= all (select k.scheduledDeparture from Leg k where k.flight.id = :flightId)")
	List<Date> findDepartureTime(Integer flightId, PageRequest pageRequest);

	@Query("select l.scheduledArrival from Leg l where l.flight.id = :flightId and l.scheduledArrival >= all (select l.scheduledArrival from Leg l where l.flight.id = :flightId)")
	List<Date> findArrivalTime(Integer flightId, PageRequest pageRequest);

	@Query("select count(l) - 1 from Leg l where l.flight.id = :flightId")
	Integer findLayovers(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	List<Leg> findAllLegsByFlightId(Integer flightId);

	@Query("select count(l) from Leg l where l.flightNumber = :flightNumber")
	Integer countSameFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.aircraft.id = :aircraftId and l.id != :id and l.draftMode = false")
	Collection<Leg> findLegsByAircraftId(Integer aircraftId, Integer id);

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode = false and l.id != :id")
	Collection<Leg> findOtherLegsByFlightId(Integer flightId, Integer id);
}
