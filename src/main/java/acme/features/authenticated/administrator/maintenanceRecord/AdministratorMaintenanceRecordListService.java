
package acme.features.authenticated.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordListService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;

		maintenanceRecords = this.repository.findMaintenanceRecordsPublished();

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDate", "estimatedCost");

		super.addPayload(dataset, maintenanceRecord, "moment", "status", "nextInspectionDate", "estimatedCost", "aircraft.registrationNumber", "aircraft.airline.iataCode", "technician.identity.fullName");
		super.getResponse().addData(dataset);
	}
}
