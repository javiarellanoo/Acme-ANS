
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			claimId = super.getRequest().getData("claim", int.class);
			claim = this.repository.findClaimById(claimId);

			status = claimId == 0 || claim != null;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tLog;

		tLog = new TrackingLog();

		super.getBuffer().addData(tLog);
	}

	@Override
	public void bind(final TrackingLog tLog) {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claim", int.class);
		claim = this.repository.findClaimById(claimId);

		super.bindObject(tLog, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		tLog.setClaim(claim);
		tLog.setDraftMode(true);
		tLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final TrackingLog tLog) {

	}

	@Override
	public void perform(final TrackingLog tLog) {
		this.repository.save(tLog);
	}

	@Override
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;
		Collection<Claim> claims;
		SelectChoices claimChoices;
		SelectChoices statusChoices;

		claims = this.repository.findClaimsByAssistanceAgentId(super.getRequest().getPrincipal().getActiveRealm().getId());
		claimChoices = SelectChoices.from(claims, "id", tLog.getClaim());
		statusChoices = SelectChoices.from(TrackingLogStatus.class, tLog.getStatus());

		dataset = super.unbindObject(tLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", //
			"resolution", "draftMode");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);
		dataset.put("claim", claimChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
