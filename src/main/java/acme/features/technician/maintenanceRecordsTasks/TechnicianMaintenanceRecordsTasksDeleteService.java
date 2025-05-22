
package acme.features.technician.maintenanceRecordsTasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordsTasksDeleteService extends AbstractGuiService<Technician, MaintenanceRecordsTasks> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordsTasksRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		boolean status;
		int technicianId;
		int taskId;
		Task task;
		boolean taskStatus;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		if (super.getRequest().getMethod().equals("GET"))
			taskStatus = true;
		else {
			technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

			taskId = super.getRequest().getData("task", int.class);
			task = this.repository.findValidTaskByIdAndMaintenanceRecord(taskId, maintenanceRecordId);
			taskStatus = taskId == 0 || task != null;
		}

		status = maintenanceRecord.getDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()) && taskStatus;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;
		MaintenanceRecordsTasks relationship;

		id = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		relationship = new MaintenanceRecordsTasks();
		relationship.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(relationship);
	}

	@Override
	public void bind(final MaintenanceRecordsTasks relationship) {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("task", int.class);
		task = this.repository.findTaskById(taskId);

		relationship.setTask(task);
	}

	@Override
	public void validate(final MaintenanceRecordsTasks relationship) {
		boolean exists;

		if (relationship.getTask() != null) {
			exists = this.repository.findByMaintenanceRecordAndTask(relationship.getMaintenanceRecord().getId(), relationship.getTask().getId()) != null;
			super.state(exists, "task", "technician.maintenance-records-tasks.form.error.not-null");
		} else
			super.state(false, "task", "technician.maintenance-records-tasks.form.error.not-null");

	}

	@Override
	public void perform(final MaintenanceRecordsTasks relationship) {
		MaintenanceRecordsTasks maintenanceRecordsTasks;

		maintenanceRecordsTasks = this.repository.findByMaintenanceRecordAndTask(relationship.getMaintenanceRecord().getId(), relationship.getTask().getId());

		if (maintenanceRecordsTasks != null)
			this.repository.delete(maintenanceRecordsTasks);
	}

	@Override
	public void unbind(final MaintenanceRecordsTasks relationship) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> tasks;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		tasks = this.repository.findTasksByMaintenanceRecordIdTask(maintenanceRecordId);
		taskChoices = SelectChoices.from(tasks, "description", relationship.getTask());

		dataset = super.unbindObject(relationship);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}
}
