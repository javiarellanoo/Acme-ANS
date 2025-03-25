
package acme.dashboards.manager;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	public Integer				managerRanking;
	public Integer				yearsToRetire;
	public Double				onTimeDelayedRatio;
	public String				mostPopularAirport;
	public String				lessPopularAirport;
	public Map<String, Integer>	numberOfLegsPerStatus;
	public Double				avgCostOfFlights;
	public Double				maxCostOfFlights;
	public Double				minCostOfFlights;
	public Double				stdvCostOfFlights;
}
