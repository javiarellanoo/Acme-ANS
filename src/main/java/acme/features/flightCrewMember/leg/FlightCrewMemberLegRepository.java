
package acme.features.flightCrewMember.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(int id);

	@Query("select a from FlightAssignment a where a.leg.id =:legId and a.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> findAssignmentsByLegAndMemberId(int legId, int memberId);

	@Query("select fcm from FlightCrewMember fcm where fcm.id =:id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("select a from Aircraft a where a.airline.id =:airlineId")
	Collection<Aircraft> findAircraftsByAirlineId(Integer airlineId);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();
}
