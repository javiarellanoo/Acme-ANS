
package acme.constraints;

import java.util.Comparator;
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

		else if (trackingLog.getStatus() != null && trackingLog.getClaim() != null) {

			// Status check

			boolean correctStatus;

			if (trackingLog.getResolutionPercentage() == 100.00)
				correctStatus = !trackingLog.getStatus().equals(TrackingLogStatus.PENDING);
			else
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

			super.state(context, correctStatus, "status", "acme.validation.trackingLog.status.message");

			// Resolution check

			if (trackingLog.getResolutionPercentage() == 100.00) {
				boolean hasResolution = !StringHelper.isBlank(trackingLog.getResolution());
				super.state(context, hasResolution, "resolution", "acme.validation.trackingLog.resolution.message");
			}

			// Published check
			if (!trackingLog.getDraftMode()) {
				boolean claimPublished = !trackingLog.getClaim().getDraftMode();
				super.state(context, claimPublished, "publish", "acme.validation.trackingLog.draftMode.message");
			}

			// Unique dissatisfaction check

			if (trackingLog.getStatus().equals(TrackingLogStatus.DISSATISFACTION)) {
				boolean isPublished = this.repository.findAllByClaimId(trackingLog.getClaim().getId()) //
					.stream().anyMatch(t -> !t.getDraftMode());
				super.state(context, isPublished, "status", "acme.validation.trackingLog.draftModeDissatisfaction.message");

				boolean isUnique = this.repository.findAllByClaimId(trackingLog.getClaim().getId()) //
					.stream().filter(t -> t.getId() != trackingLog.getId()) //
					.noneMatch(t -> t.getStatus().equals(TrackingLogStatus.DISSATISFACTION));
				super.state(context, isUnique, "status", "acme.validation.trackingLog.statusDissatisfaction");

			}

			// Incrementing resolution percentage check

			boolean greaterPercentage = true;

			if (trackingLog.getResolutionPercentage() != null && trackingLog.getLastUpdateMoment() != null) {
				List<TrackingLog> allByClaim = this.repository.findAllByClaimId(trackingLog.getClaim().getId());
				List<TrackingLog> beforeActual = allByClaim.stream().filter(t -> t.getLastUpdateMoment().before(trackingLog.getLastUpdateMoment())).collect(Collectors.toList());

				if (!beforeActual.isEmpty()) {
					beforeActual.sort(Comparator.comparing(TrackingLog::getResolutionPercentage).reversed());
					TrackingLog previous = beforeActual.get(0);

					if (trackingLog.getStatus().equals(TrackingLogStatus.DISSATISFACTION))
						greaterPercentage = allByClaim.stream().anyMatch(t -> t.getResolutionPercentage().equals(Double.valueOf(100)));
					else
						greaterPercentage = trackingLog.getResolutionPercentage() > previous.getResolutionPercentage();
				}
			}

			super.state(context, greaterPercentage, "resolutionPercentage", "acme.validation.trackingLog.percentage.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
