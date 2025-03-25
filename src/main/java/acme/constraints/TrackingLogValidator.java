
package acme.constraints;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state

	@Autowired
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

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean hasResolution = true;
			Boolean correctStatus;
			boolean greaterPercentage = false;

			if (trackingLog.getResolutionPercentage() == 100.00) {
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.ACCEPTED) || trackingLog.getStatus().equals(TrackingLogStatus.REJECTED);
				hasResolution = !StringHelper.isBlank(trackingLog.getResolution());
			} else
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

			if (trackingLog.getResolutionPercentage() != null) {
				List<TrackingLog> allOrdered = this.repository.findAllOrderedByIndex(trackingLog.getClaim().getId());

				TrackingLog previous = allOrdered.stream().filter(t -> t.getLastUpdateMoment().before(trackingLog.getLastUpdateMoment())).collect(Collectors.toList()).get(0);

				greaterPercentage = trackingLog.getResolutionPercentage() > previous.getResolutionPercentage();
			}

			super.state(context, correctStatus, "status", "acme.validation.trackingLog.status.message");
			super.state(context, hasResolution, "resolution", "acme.validation.trackingLog.resolution.message");
			super.state(context, greaterPercentage, "resolutionPercentage", "acme.validation.trackingLog.percentage.message");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
