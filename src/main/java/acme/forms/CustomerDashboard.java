package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

    // Serialisation version --------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    private String[] lastFiveDestinations;
    private Double moneySpentLastYear;
    private Map<String, Integer> travelClassGrouped;
    private Double averageBookingCostLastFiveYears;
    private Double minBookingCostLastFiveYears;
    private Double maxBookingCostLastFiveYears;
    private Integer countBookingCostLastFiveYears;
    private Double stdDevBookingCostLastFiveYears;
    private Integer countPassengers;
    private Double averagePassengers;
    private Integer minPassengers;
    private Integer maxPassengers;
    private Double stdDevPassengers;
}
