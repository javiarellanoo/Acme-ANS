
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	//AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int tLogId;
		int agentId;
		TrackingLog tLog;
		AssistanceAgent agent;

		tLogId = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(tLogId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		status = tLog != null && tLog.getClaim() != null && tLog.getClaim().getAssistanceAgent() != null && tLog.getClaim().getAssistanceAgent().equals(agent);

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
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;
		SelectChoices statusChoices;

		statusChoices = SelectChoices.from(TrackingLogStatus.class, tLog.getStatus());

		dataset = super.unbindObject(tLog, "lastUpdateMoment", "creationMoment", "stepUndergoing", "resolutionPercentage", //
			"resolution", "draftMode", "claim.assistanceAgent.identity.fullName", "claim.id");
		dataset.put("statuses", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		if (tLog.getClaim() != null)
			super.getResponse().addGlobal("claimDraftMode", tLog.getClaim().getDraftMode());
		else
			super.getResponse().addGlobal("claimDraftMode", true);
		super.getResponse().addData(dataset);
	}
}
