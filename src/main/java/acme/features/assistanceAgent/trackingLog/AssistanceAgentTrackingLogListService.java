
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
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
		boolean status;
		int agentId;
		int claimId;
		Claim claim;
		AssistanceAgent agent;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);
		claimId = super.getRequest().getData("claim", int.class);
		claim = this.repository.findClaimById(claimId);
		status = claim != null && (!claim.getDraftMode() || claim.getAssistanceAgent().equals(agent));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> tLogs;
		int claimId;

		claimId = super.getRequest().getData("claim", int.class);
		tLogs = this.repository.findTrackingLogsByClaim(claimId);

		super.getBuffer().addData(tLogs);
	}

	@Override
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;

		dataset = super.unbindObject(tLog, "claim.id", "resolutionPercentage", "status", "lastUpdateMoment", "draftMode");
		super.addPayload(dataset, tLog, //
			"lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "status", "resolution", //
			"draftMode", "claim.assistanceAgent.identity.fullName", "claim.leg.flightNumber");

		super.getResponse().addData(dataset);
	}

}
