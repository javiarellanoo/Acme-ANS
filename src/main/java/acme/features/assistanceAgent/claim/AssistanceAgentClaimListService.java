
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal State

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		Collection<Claim> completedClaims;
		Collection<Claim> undergoing;
		boolean completed;
		int assistanceId;

		completed = super.getRequest().getData("completed", boolean.class);
		assistanceId = super.getRequest().getPrincipal().getActiveRealm().getId();

		claims = this.repository.findClaimsByAssistanceAgent(assistanceId);
		completedClaims = claims.stream().filter(c -> !c.getStatus().equals("PENDING")).toList();
		undergoing = claims.stream().filter(c -> c.getStatus().equals("PENDING")).toList();

		claims = completed ? completedClaims : undergoing;

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "id", "registrationMoment", "status", "type", "draftMode");
		super.addPayload(dataset, claim, //
			"registrationMoment", "passengerEmail", "type", "draftMode", "description", "assistanceAgent.identity.fullName", "leg.flightNumber", "id");

		super.getResponse().addData(dataset);
	}

}
