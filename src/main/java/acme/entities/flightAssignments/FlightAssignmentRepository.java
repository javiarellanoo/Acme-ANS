
package acme.entities.flightAssignments;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssignment a where a.leg.id =:legId and a.duty = acme.entities.flightAssignments.Duty.PILOT and a.draftMode = false")
	FlightAssignment findPilotAssignmentsByLegId(int legId);

	@Query("select a from FlightAssignment a where a.leg.id =:legId and a.duty = acme.entities.flightAssignments.Duty.COPILOT and a.draftMode = false")
	FlightAssignment findCopilotAssignmentsByLegId(int legId);
}
