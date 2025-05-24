
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						ratioClaimsSuccesfullyResolved;
	Double						ratioClaimsRejected;
	Map<String, Integer>		monthsWithMoreClaims;
	Statistics					statisticsNumberTlogsClaim;
	Statistics					statisticsOfClaimsDuringLastMonth;
}
