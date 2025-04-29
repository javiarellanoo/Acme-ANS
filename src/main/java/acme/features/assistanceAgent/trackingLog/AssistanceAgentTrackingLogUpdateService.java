
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
		TrackingLog tLog;
		AssistanceAgent agent;
		Claim claim;

		tLogId = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(tLogId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		claimId = super.getRequest().getData("claim", int.class);
		claim = this.repository.findClaimById(claimId);

		status = tLog != null && tLog.getClaim() != null && tLog.getClaim().getAssistanceAgent() != null //
			&& tLog.getClaim().getAssistanceAgent().equals(agent) && claim != null;

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
