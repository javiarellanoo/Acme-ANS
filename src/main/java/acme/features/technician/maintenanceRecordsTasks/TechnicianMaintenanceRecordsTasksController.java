
package acme.features.technician.maintenanceRecordsTasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenanceRecordsTasksController extends AbstractGuiController<Technician, MaintenanceRecordsTasks> {

	// Internal state ---------------------------------------------------------

	@Autowired
	TechnicianMaintenanceRecordsTasksCreateService	createService;

	@Autowired
	TechnicianMaintenanceRecordsTasksDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
