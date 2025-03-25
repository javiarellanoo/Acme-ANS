
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidMaintenanceRecord;
import acme.entities.aircrafts.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidMaintenanceRecord
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Automapped
	private Date					moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordStatus	status;

	@Mandatory
	@ValidMoment
	@Automapped
	private Date					nextInspectionDate;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money					estimatedCost;

	@Optional
	@ValidString
	@Automapped
	private String					notes;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft				aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician				technician;
}
