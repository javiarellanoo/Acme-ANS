
package acme.entities.forecast;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Forecast extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				city;

	@Optional
	@ValidNumber
	@Automapped
	private Integer				weatherCode;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				predictionMoment;

	@Optional
	@ValidNumber
	@Automapped
	private Double				maximumTemperature;

	@Optional
	@ValidNumber
	@Automapped
	private Double				minimumTemperature;

	@Optional
	@ValidNumber
	@Automapped
	private Double				precipitationSum;

	@Optional
	@ValidNumber
	@Automapped
	private Double				windSpeed;

	@Optional
	@ValidNumber
	@Automapped
	private Double				windGusts;

}
