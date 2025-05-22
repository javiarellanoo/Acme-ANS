
package acme.features.assistanceAgent.dashboard;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int agentId;
		AssistanceAgentDashboard dashboard;
		Collection<Claim> claims;
		PageRequest pageClaimsMonths;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findClaimsByAssistanceAgentId(agentId);
		pageClaimsMonths = PageRequest.of(0, 3);

		double ratioClaimsSuccesfullyResolved = this.repository.ratioClaimsSuccesfullyResolvedByAgent(agentId);

		long rejected = claims.stream().map(c -> c.getStatus()).filter(s -> s.equals("REJECTED")).count();
		long totalClaimsPublished = claims.stream().filter(c -> !c.getDraftMode()).count();
		double ratioClaimsRejected = totalClaimsPublished == 0 ? 0.0 : (double) rejected / totalClaimsPublished;

		List<Object[]> top3Months = this.repository.top3MonthsWithMoreClaims(pageClaimsMonths);
		Map<String, Integer> monthsWithMoreClaims = top3Months.stream().collect(Collectors.toMap(t -> String.format("%s-%02d", t[0], t[1]), t -> ((Number) t[2]).intValue(), (t1, t2) -> t1, LinkedHashMap::new));

		dashboard = new AssistanceAgentDashboard();
		dashboard.setRatioClaimsSuccesfullyResolved(ratioClaimsSuccesfullyResolved);
		dashboard.setRatioClaimsRejected(ratioClaimsRejected);
		dashboard.setMonthsWithMoreClaims(monthsWithMoreClaims);
	}

}
