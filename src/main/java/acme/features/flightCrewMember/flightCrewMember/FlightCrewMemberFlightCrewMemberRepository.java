
package acme.features.flightCrewMember.flightCrewMember;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightCrewMemberRepository extends AbstractRepository {

	@Query("select fcm from FlightCrewMember fcm where fcm.id =:id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

}
