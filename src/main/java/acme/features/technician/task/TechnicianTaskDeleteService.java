
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Task task;
		String method;

		method = super.getRequest().getMethod();

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		if (task == null)
			status = false;
		else
			status = task.getDraftMode() && super.getRequest().getPrincipal().hasRealm(task.getTechnician()) && !method.equals("GET");

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedHoursDuration");
	}

	@Override
	public void validate(final Task task) {

	}

	@Override
	public void perform(final Task task) {
		Collection<MaintenanceRecordsTasks> relationships;

		relationships = this.repository.findRelationshipsByTask(task.getId());

		this.repository.deleteAll(relationships);
		this.repository.delete(task);
	}
}
