
package acme.features.assistanceAgent.dashboard;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.forms.AssistanceAgentDashboard;
import acme.forms.Statistics;
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
		Statistics statisticsNumberTlogsClaim;
		Statistics statisticsOfClaimsDuringLastMonth;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findClaimsByAssistanceAgentId(agentId);
		pageClaimsMonths = PageRequest.of(0, 3);

		double ratioClaimsSuccesfullyResolved = this.repository.ratioClaimsSuccesfullyResolvedByAgent(agentId);

		long rejected = claims.stream().map(c -> c.getStatus()).filter(s -> s.equals("REJECTED")).count();
		long totalClaimsPublished = claims.stream().filter(c -> !c.getDraftMode()).count();
		double ratioClaimsRejected = totalClaimsPublished == 0 ? 0.0 : (double) rejected / totalClaimsPublished;

		List<Object[]> top3Months = this.repository.top3MonthsWithMoreClaims(pageClaimsMonths);
		Map<String, Integer> monthsWithMoreClaims = top3Months.stream().collect(Collectors.toMap(t -> String.format("%s-%02d", t[0], t[1]), t -> ((Number) t[2]).intValue(), (t1, t2) -> t1, LinkedHashMap::new));

		List<Double> numberTlogsByClaim = this.repository.findNumberTLogsByClaimOfAgent(agentId);
		statisticsNumberTlogsClaim = new Statistics();
		statisticsNumberTlogsClaim.setData(numberTlogsByClaim);

		Map<Integer, Long> lastMonthNumberOfWeekPerClaim = claims.stream().filter(c -> c.getRegistrationMoment().getMonth() == MomentHelper.getCurrentMoment().getMonth()) //
			.map(c -> c.getRegistrationMoment().getDay() / 7 + 1).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		List<Double> claimsPerWeekCount = IntStream.rangeClosed(1, 5).mapToObj(i -> lastMonthNumberOfWeekPerClaim.getOrDefault(i, 0L).doubleValue()).collect(Collectors.toList());
		statisticsOfClaimsDuringLastMonth = new Statistics();
		statisticsOfClaimsDuringLastMonth.setData(claimsPerWeekCount);

		dashboard = new AssistanceAgentDashboard();
		dashboard.setRatioClaimsSuccesfullyResolved(ratioClaimsSuccesfullyResolved);
		dashboard.setRatioClaimsRejected(ratioClaimsRejected);
		dashboard.setMonthsWithMoreClaims(monthsWithMoreClaims);
		dashboard.setStatisticsNumberTlogsClaim(statisticsNumberTlogsClaim);
		dashboard.setStatisticsOfClaimsDuringLastMonth(statisticsOfClaimsDuringLastMonth);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "ratioClaimsSuccesfullyResolved", "ratioClaimsRejected", "monthsWithMoreClaims", "statisticsNumberTlogsClaim", "statisticsOfClaimsDuringLastMonth");

		super.getResponse().addData(dataset);
	}

}
