
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	public Double				reviewRatio;
	public Double				airlinesWithMailAndPhoneRatio;
	public Double				activeAircraftsRatio;
	public Double				nonActiveAircraftsRatio;
	public Map<String, Integer>	numberOfAirlinesPerType;
	public Map<String, Integer>	numberOfAirportsPerScope;
	public Long					numberOfReviews;
	public Double				averageNumberOfReviews;
	public Long					minimumNumberOfReviews;
	public Long					maximumNumberOfReviews;
	public Double				stdDevReviews;
}
