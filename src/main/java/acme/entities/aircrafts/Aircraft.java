
package acme.entities.aircrafts;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;

public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50, min = 1)
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(max = 50, min = 1)
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(max = 255, min = 1)
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(max = 50000, min = 2000)
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString(max = 255, min = 0)
	@Automapped
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;
}
