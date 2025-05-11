
package acme.forms;

import java.util.Collection;
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
	public Double				onTimeRatio;
	public Double				delayedRatio;
	public String	mostPopularAirport;
	public String	lessPopularAirport;
	public Map<String, Integer>	numberOfLegsPerStatus;
	public Map<String, Double>	averageCostOfFlightsPerCurrency;
	public Map<String, Double>	minimumCostOfFlightsPerCurrency;
	public Map<String, Double>	maximumCostOfFlightsPerCurrency;
	public Map<String, Double>	stdDevCostOfFlightsPerCurrency;
	private Collection<String>	currencies;
	private String				selectedCurrency;
}
