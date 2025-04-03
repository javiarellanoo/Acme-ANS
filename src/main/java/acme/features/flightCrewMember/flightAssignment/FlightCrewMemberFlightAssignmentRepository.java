
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssigment a")
	Collection<FlightAssignment> findAllAssignments();

}
