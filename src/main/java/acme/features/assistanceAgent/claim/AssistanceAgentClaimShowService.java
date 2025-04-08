
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstracGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;
		AssistanceAgent assistanceAgent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();

		status = claim != null && (!claim.getDraftMode() || assistanceAgent.getId() == super.getRequest().getPrincipal().getActiveRealm().getId());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesType;

		legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		choicesType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode", "status", "assistanceAgent.identity.fullName", "leg.flightNumber");
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);
		dataset.put("id", claim.getId());
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);

		super.getResponse().addData(dataset);
	}

}
