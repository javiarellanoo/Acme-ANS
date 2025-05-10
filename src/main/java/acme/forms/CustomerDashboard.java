package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.Collection;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

    // Serialisation version --------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    private String[] lastFiveDestinations;
    private Map<String, Double> moneySpentLastYearByCurrency;
    private Map<String, Integer> travelClassGrouped;
    private Map<String, Double> averageBookingCostLastFiveYearsByCurrency;
    private Map<String, Double> minBookingCostLastFiveYearsByCurrency;
    private Map<String, Double> maxBookingCostLastFiveYearsByCurrency;
    private Map<String, Integer> countBookingCostLastFiveYearsByCurrency;
    private Map<String, Double> stdDevBookingCostLastFiveYearsByCurrency;
    private Integer countPassengers;
    private Double averagePassengers;
    private Integer minPassengers;
    private Integer maxPassengers;
    private Double stdDevPassengers;
    private Collection<String> currencies;
    private String selectedCurrency;
}
