
package acme.features.flightCrewMember.flightCrewMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightCrewMemberShowService extends AbstractGuiService<FlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberFlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightCrewMember fcm;
		int memberId;
		FlightCrewMember loggedMember;

		masterId = super.getRequest().getData("id", int.class);
		fcm = this.repository.findCrewMemberById(masterId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		loggedMember = this.repository.findCrewMemberById(memberId);
		status = fcm != null && fcm.getAirline().getId() == loggedMember.getAirline().getId();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		FlightCrewMember fcm;
		int id;

		id = super.getRequest().getData("id", int.class);
		fcm = this.repository.findCrewMemberById(id);

		super.getBuffer().addData(fcm);

	}

	@Override
	public void unbind(final FlightCrewMember member) {
		Dataset dataset;
		SelectChoices choicesAirline;
		SelectChoices choicesStatus;
		Collection<Airline> airlines;
		airlines = this.repository.findAllAirlines();

		choicesAirline = SelectChoices.from(airlines, "name", member.getAirline());
		choicesStatus = SelectChoices.from(AvailabilityStatus.class, member.getAvailabilityStatus());
		dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "identity.fullName", "airline.name");
		dataset.put("airline", choicesAirline.getSelected().getKey());
		dataset.put("airlines", choicesAirline);
		dataset.put("availabilityStatuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

}
