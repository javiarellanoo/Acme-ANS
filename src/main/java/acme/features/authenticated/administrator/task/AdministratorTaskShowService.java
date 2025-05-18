
package acme.features.authenticated.administrator.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskType;

@GuiService
public class AdministratorTaskShowService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorMaintenanceRecordsTasksRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findValidTaskById(taskId);

		status = task != null && !task.getDraftMode();

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
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "description", "priority", "estimatedHoursDuration", "draftMode");
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("id", task.getId());

		super.getResponse().addData(dataset);
	}
}
