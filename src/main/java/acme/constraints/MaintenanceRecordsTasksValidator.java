
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.maintenanceRecordsTasks.MaintenanceRecordsTasks;

@Validator
public class MaintenanceRecordsTasksValidator extends AbstractValidator<ValidMaintenanceRecordsTasks, MaintenanceRecordsTasks> {

	@Override
	protected void initialise(final ValidMaintenanceRecordsTasks annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecordsTasks relationship, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (relationship == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean taskPublished;

			taskPublished = relationship.getMaintenanceRecord().getDraftMode() || !relationship.getTask().getDraftMode();

			super.state(context, taskPublished, "task", "acme.validation.maintenanceRecordsTasks.task.message");

		}
		result = !super.hasErrors(context);

		return result;
	}

}
