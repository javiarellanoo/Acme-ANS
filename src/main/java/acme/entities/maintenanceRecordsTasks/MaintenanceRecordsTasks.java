
package acme.entities.maintenanceRecordsTasks;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidMaintenanceRecordsTasks;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidMaintenanceRecordsTasks
@Table(indexes = {
	@Index(columnList = "task_id"), @Index(columnList = "maintenance_record_id"), @Index(columnList = "task_id,maintenance_record_id")
})
public class MaintenanceRecordsTasks extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Task				task;
}
