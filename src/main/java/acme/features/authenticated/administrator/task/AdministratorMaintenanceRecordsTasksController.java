
package acme.features.authenticated.administrator.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.tasks.Task;

@GuiController
public class AdministratorMaintenanceRecordsTasksController extends AbstractGuiController<Administrator, Task> {

	@Autowired
	private AdministratorMaintenanceRecordsTasksListService	listService;

	@Autowired
	private AdministratorTaskShowService					showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
