
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<TrackingLog> tLogs;
		boolean published;
		int assistanceId;

		published = super.getRequest().getData("published", boolean.class);
		assistanceId = super.getRequest().getPrincipal().getActiveRealm().getId();
		tLogs = published ? this.repository.findTrackingLogsPublished() : this.repository.findTrackingLogsByAssistanceAgent(assistanceId);

		super.getBuffer().addData(tLogs);
	}

	@Override
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;

		dataset = super.unbindObject(tLog, "claim.id", "resolutionPercentage", "status", "lastUpdateMoment");
		super.addPayload(dataset, tLog, //
			"lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "status", "resolution", //
			"draftMode", "claim.assistanceAgent.identity.fullName", "claim.leg.flightNumber");

		super.getResponse().addData(dataset);
	}

}
