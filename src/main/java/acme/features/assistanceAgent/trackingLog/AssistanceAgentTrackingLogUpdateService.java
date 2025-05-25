
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		TrackingLog tLog;
		AssistanceAgent agent;

		tLogId = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(tLogId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		status = tLog != null && tLog.getClaim() != null && tLog.getClaim().getAssistanceAgent() != null //
			&& tLog.getClaim().getAssistanceAgent().equals(agent) && tLog.getDraftMode();

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
		if (tLog.getClaim().getDraftMode())
			super.bindObject(tLog, "stepUndergoing", "resolutionPercentage", "status", "resolution");
		else
			super.bindObject(tLog, "stepUndergoing", "resolutionPercentage", "resolution");
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

		super.getResponse().addGlobal("claimDraftMode", tLog.getClaim().getDraftMode());
		super.getResponse().addData(dataset);
	}
}
