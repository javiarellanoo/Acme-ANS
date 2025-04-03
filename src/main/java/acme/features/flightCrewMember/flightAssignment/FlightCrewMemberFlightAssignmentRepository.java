
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("select a from FlightAssignment a")
	Collection<FlightAssignment> findAllAssignments();

}
