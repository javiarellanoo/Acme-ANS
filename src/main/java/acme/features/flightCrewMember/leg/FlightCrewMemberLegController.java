
package acme.features.flightCrewMember.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.FlightCrewMember;

@GuiController
public class FlightCrewMemberLegController extends AbstractGuiController<FlightCrewMember, Leg> {

	@Autowired
	private FlightCrewMemberLegShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
