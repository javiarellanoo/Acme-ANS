
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean published;
		boolean status;

		published = super.getRequest().getData("published", Boolean.class);

		status = published != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		boolean published;
		int technicianId;

		published = super.getRequest().getData("published", boolean.class);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		tasks = published ? this.repository.findTasksPublished() : this.repository.findTasksByTechnicianId(technicianId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedHoursDuration");

		super.addPayload(dataset, task, "type", "description", "priority", "estimatedHoursDuration", "technician.identity.fullName");
		super.getResponse().addData(dataset);
	}
}
