
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
		super.getResponse().setAuthorised(true);
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

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		tasks = this.repository.findTasks(maintenanceRecordId);
		taskChoices = SelectChoices.from(tasks, "description", relationship.getTask());

		dataset = super.unbindObject(relationship);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}
}
