
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// ConstraintValidator interface

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean hasResolution = true;
			Boolean correctStatus;

			if (trackingLog.getResolutionPercentage() == 100.00) {
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.ACCEPTED) || trackingLog.getStatus().equals(TrackingLogStatus.REJECTED);
				hasResolution = trackingLog.getResolution() != null && !trackingLog.getResolution().isEmpty();
			} else
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

			super.state(context, !correctStatus, "status", "acme.validation.trackingLog.status.message");
			super.state(context, !hasResolution, "resolution", "acme.validation.trackingLog.resolution.message");

		}

		result = !super.hasErrors(context);

		return result;
	}

}
