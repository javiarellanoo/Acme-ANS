
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);

			status = legId == 0 || leg != null && !leg.getDraftMode();
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;

		claim = new Claim();

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		int agentId;
		Leg leg;
		AssistanceAgent agent;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);

		super.bindObject(claim, "type", "passengerEmail", "description");
		claim.setLeg(leg);
		claim.setAssistanceAgent(agent);
		claim.setDraftMode(true);
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
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
		SelectChoices choicesLegs;
		SelectChoices choicesType;
		Dataset dataset;

		legs = this.repository.findAllLegsPublished();
		choicesLegs = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		choicesType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
