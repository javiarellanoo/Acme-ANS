
package acme.dashboards.flightCrewMember;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.flightAssignments.AssignmentStatus;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDasboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>							lastFiveDestinations;
	Integer									numberOfLegsWithIncidentBetween0And3;
	Integer									numberOfLegsWithIncidentBetween4And7;
	Integer									numberOfLegsWithIncidentBetween8and10;
	List<FlightCrewMember>					crewMembersAssignedInTheirLastLeg;
	Map<AssignmentStatus, FlightAssignment>	assignmentsGroupByStatus;
	Double									avgAssignmentsLastMonth;
	Integer									minAssignmentsLastMonth;
	Integer									maxAssignmentsLastMonth;
	Double									stdvAssignmentsLastMonth;

}
