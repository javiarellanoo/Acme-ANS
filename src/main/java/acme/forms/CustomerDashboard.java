package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.List;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

    // Serialisation version --------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    private String[] lastFiveDestinations;
    private Map<String, Double> moneySpentLastYearByCurrency;
    private Map<String, Integer> travelClassGrouped;
    private Map<String, Statistics> costStatisticsLastFiveYears;
    private Statistics passengersStatistics;
}
