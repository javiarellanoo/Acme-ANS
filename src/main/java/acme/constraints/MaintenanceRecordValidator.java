
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidMaintenanceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (maintenanceRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (maintenanceRecord.getNextInspectionDate() != null && maintenanceRecord.getMoment() != null) {

			Date moment = maintenanceRecord.getMoment();

			Date nextInspectionDate = maintenanceRecord.getNextInspectionDate();

			Boolean isNextInspectionAfterMoment = MomentHelper.isAfter(nextInspectionDate, moment);

			super.state(context, isNextInspectionAfterMoment, "nextInspectionDate", "acme.validation.maintenanceRecord.nextInspectionDate.message");

		}
		result = !super.hasErrors(context);

		return result;
	}
}
