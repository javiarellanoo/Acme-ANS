
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fcm.airline.id from FlightCrewMember fcm where fcm.id =:id")
	Integer findAirlineIdByFlightCrewMemberId(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("select a from FlightAssignment a where a.flightCrewMember.airline.id = :airlineId")
	Collection<FlightAssignment> findAssignmentsByAirlineId(int airlineId);

	@Query("select l from Leg l where l.flight.airline.id =:airlineId and l.draftMode = false")
	Collection<Leg> findLegsByAirlineId(int airlineId);

	@Query("select fcm from FlightCrewMember fcm where fcm.airline.id =:airlineId")
	Collection<FlightCrewMember> findCrewMembersByAirlineId(int airlineId);

	@Query("select l from Leg l where l.id =:id")
	Leg findLegById(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.id =:id")
	FlightCrewMember findCrewMemberById(int id);

}
