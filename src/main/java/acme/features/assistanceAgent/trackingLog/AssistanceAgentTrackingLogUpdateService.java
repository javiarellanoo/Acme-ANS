
package acme.features.assistanceAgent.trackingLog;

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
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	//AbstractGuiService
	@Override
	public void authorise() {
		boolean status;
		int tLogId;
		int agentId;
		int claimId;
		int masterId;
		TrackingLog tLog;
		AssistanceAgent agent;

		tLogId = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(tLogId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		claimId = super.getRequest().getData("claim", int.class);
		masterId = super.getRequest().getData("masterId", int.class);

		status = tLog != null && tLog.getClaim() != null && tLog.getClaim().getAssistanceAgent() != null //
			&& tLog.getClaim().getAssistanceAgent().equals(agent) && claimId == masterId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(id);

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
	}

	@Override
	public void validate(final TrackingLog tLog) {

	}

	@Override
	public void perform(final TrackingLog tLog) {
		tLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		this.repository.save(tLog);
	}

	@Override
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;
		SelectChoices statusChoices;

		statusChoices = SelectChoices.from(TrackingLogStatus.class, tLog.getStatus());

		dataset = super.unbindObject(tLog, "lastUpdateMoment", "creationMoment", "stepUndergoing", "resolutionPercentage", //
			"resolution", "draftMode");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("claim", tLog.getClaim());

		if (tLog.getClaim() != null)
			super.getResponse().addGlobal("isClaimDraftMode", tLog.getClaim().getDraftMode());
		else
			super.getResponse().addGlobal("isClaimDraftMode", false);
		super.getResponse().addData(dataset);
	}
}
