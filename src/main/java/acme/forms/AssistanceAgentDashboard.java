
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.claims.Claim;
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
	Map<Claim, Statistics>		statisticsPerClaimOfLogs;
	Statistics					statisticsOfClaims;
}
