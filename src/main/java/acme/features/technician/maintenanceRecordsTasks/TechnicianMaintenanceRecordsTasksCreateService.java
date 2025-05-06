
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
public class TechnicianMaintenanceRecordsTasksCreateService extends AbstractGuiService<Technician, MaintenanceRecordsTasks> {

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

		if (super.getRequest().getMethod().equals("GET"))
			taskStatus = true;
		else {
			technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

			taskId = super.getRequest().getData("task", int.class);
			task = this.repository.findValidTaskById(taskId, technicianId);
			taskStatus = taskId == 0 || task != null;
		}

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		status = maintenanceRecord.getDraftMode() && super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()) && super.getRequest().getPrincipal().getActiveRealm().getId() == maintenanceRecord.getTechnician().getId() && taskStatus;

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
		super.bindObject(relationship, "task");
	}

	@Override
	public void validate(final MaintenanceRecordsTasks relationship) {

	}

	@Override
	public void perform(final MaintenanceRecordsTasks relationship) {
		this.repository.save(relationship);
	}

	@Override
	public void unbind(final MaintenanceRecordsTasks relationship) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> tasks;
		int maintenanceRecordId;
		int technicianId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		technicianId = relationship.getMaintenanceRecord().getTechnician().getId();

		tasks = this.repository.findTasks(maintenanceRecordId, technicianId);
		taskChoices = SelectChoices.from(tasks, "description", relationship.getTask());

		dataset = super.unbindObject(relationship);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}
}
