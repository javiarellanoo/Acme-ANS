
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
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		status = claim != null && claim.getDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent()) && claim.getAssistanceAgent().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

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
		AssistanceAgent assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		int legId;
		Leg leg;

		legId = super.getRequest().getData("legs", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		claim.setAssistanceAgent(assistanceAgent);
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {

	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> relationships;

		relationships = this.repository.findTrackingLogsByClaim(claim.getId());

		this.repository.deleteAll(relationships);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		SelectChoices choicesType;
		SelectChoices choicesLeg;
		Dataset dataset;

		legs = this.repository.findAllLegs();
		choicesLeg = SelectChoices.from(legs, "flightNumebr", claim.getLeg());

		choicesType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("AssistanceAgent", claim.getAssistanceAgent());
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);
		dataset.put("leg", choicesLeg.getSelected().getKey());
		dataset.put("legs", choicesLeg);

		super.getResponse().addData(dataset);
	}

}
