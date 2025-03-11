
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state

	private TrackingLogRepository repository;

	// ConstraintValidator interface


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;

		Boolean hasResolution = true;
		Boolean correctStatus;
		Boolean greaterPercentage;

		if (trackingLog.getResolutionPercentage() == 100.00) {
			correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.ACCEPTED) || trackingLog.getStatus().equals(TrackingLogStatus.REJECTED);
			hasResolution = trackingLog.getResolution() != null && !StringHelper.isBlank(trackingLog.getResolution());
		} else
			correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

		greaterPercentage = trackingLog.getResolutionPercentage() > this.repository.getLastTrakingLogPercentage();

		super.state(context, correctStatus, "status", "acme.validation.trackingLog.status.message");
		super.state(context, hasResolution, "resolution", "acme.validation.trackingLog.resolution.message");
		super.state(context, greaterPercentage, "resolutionPercentage", "acme.validation.trackingLog.percentage.message");

		result = !super.hasErrors(context);

		return result;
	}

}
