
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fcm.airline.id from FlightCrewMember fcm where fcm.id =:id")
	Integer findAirlineIdByFlightCrewMemberId(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("select al from ActivityLog al where al.assignment.id =:assignmentId")
	Collection<ActivityLog> findActivityLogByAssignmentId(int assignmentId);

	@Query("select al from ActivityLog al where al.id =:id")
	ActivityLog findActivityLogById(int id);
}
