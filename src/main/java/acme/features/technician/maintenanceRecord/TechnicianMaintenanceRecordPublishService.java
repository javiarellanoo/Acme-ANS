
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean publishable;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		Collection<Task> tasks;
		int aircraftId;
		Aircraft aircraft;
		boolean aircraftStatus = false;
		String method;

		method = super.getRequest().getMethod();

		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);

		publishable = !tasks.isEmpty() && tasks.stream().allMatch(x -> !x.getDraftMode());

		if (maintenanceRecord == null)
			status = false;
		else if (!maintenanceRecord.getDraftMode() || !super.getRequest().getPrincipal().hasRealm(technician))
			status = false;
		else if (method.equals("GET"))
			status = publishable;
		else {
			aircraftId = super.getRequest().getData("aircraft", int.class);
			aircraft = this.repository.findValidAircraftById(aircraftId);
			aircraftStatus = aircraftId == 0 || aircraft != null;

			status = publishable && aircraftStatus;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findValidAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "status", "moment", "nextInspectionDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;
		boolean publishable;
		Collection<Task> tasks;

		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecord.getId());
		publishable = maintenanceRecord.getDraftMode() && !tasks.isEmpty() && tasks.stream().allMatch(x -> !x.getDraftMode());

		aircrafts = this.repository.findAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		statusChoices = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "moment", "nextInspectionDate", "estimatedCost", "notes", "draftMode");
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("publishable", publishable);

		super.getResponse().addData(dataset);
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}
}
