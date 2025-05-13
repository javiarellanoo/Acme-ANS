
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
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		boolean externalRelation = true;
		int claimId;
		int agentId;
		int legId;
		Claim claim;
		Leg leg;
		AssistanceAgent agent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		if (super.getRequest().getMethod().equals("POST")) {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);

			externalRelation = legId == 0 || leg != null && !leg.getDraftMode() && !leg.getFlight().getDraftMode();
		}

		status = claim != null && claim.getDraftMode() && claim.getAssistanceAgent() != null && //
			claim.getAssistanceAgent().equals(agent) && externalRelation;

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
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		SelectChoices choicesType;
		SelectChoices choicesLeg;
		Dataset dataset;

		legs = this.repository.findAllLegsPublishedForFlightsPublished();
		choicesLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		choicesType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");

		dataset.put("types", choicesType);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("legs", choicesLeg);
		dataset.put("leg", choicesLeg.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
