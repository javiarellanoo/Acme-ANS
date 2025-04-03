
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
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setDraftMode(true);
		claim.setAssistanceAgent(agent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
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
		SelectChoices choicesLegs;
		SelectChoices choicesType;
		Dataset dataset;

		legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "flightNumebr", claim.getLeg());
		choicesType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("legs", choicesLegs);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
