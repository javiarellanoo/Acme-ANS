
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
		Claim claim;
		int masterId;
		Collection<TrackingLog> tLogs;
		boolean twoCompleted;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		tLogs = this.repository.findTrackingLogsByClaim(claim.getId());
		twoCompleted = tLogs.stream().filter(t -> !t.getStatus().equals(TrackingLogStatus.PENDING)).count() < 2L;

		status = claim.getAssistanceAgent().equals(super.getRequest().getPrincipal().getActiveRealm()) && twoCompleted;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tLog;
		int masterId;

		tLog = new TrackingLog();
		masterId = super.getRequest().getData("masterId", int.class);

		super.getBuffer().addData(tLog);
		tLog.setCreationMoment(MomentHelper.getCurrentMoment());
		tLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		tLog.setClaim(this.repository.findClaimById(masterId));
	}

	@Override
	public void bind(final TrackingLog tLog) {
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		super.bindObject(tLog, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		tLog.setClaim(claim);
		tLog.setDraftMode(true);
		tLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		tLog.setCreationMoment(MomentHelper.getCurrentMoment());
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
		int masterId;
		Claim claim;
		SelectChoices statusChoices;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		statusChoices = SelectChoices.from(TrackingLogStatus.class, tLog.getStatus());

		dataset = super.unbindObject(tLog, "lastUpdateMoment", "creationMoment", "stepUndergoing", "resolutionPercentage", //
			"resolution", "draftMode");
		dataset.put("claim", claim);
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		if (claim != null)
			super.getResponse().addGlobal("claimDraftMode", claim.getDraftMode());
		else
			super.getResponse().addGlobal("claimDraftMode", true);

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", masterId);
	}

}
