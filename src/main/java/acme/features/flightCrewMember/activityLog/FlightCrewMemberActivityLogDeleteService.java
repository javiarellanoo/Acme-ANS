
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int activityLogId;
		ActivityLog log;
		FlightAssignment assignment;
		int memberId;
		int airlineId;

		activityLogId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(activityLogId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByFlightCrewMemberId(memberId);
		if (log == null)
			status = false;
		else {
			assignment = log.getAssignment();
			status = log.getDraftMode() && assignment.getFlightCrewMember().getAirline().getId() == airlineId && !super.getRequest().getMethod().equals("GET");
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);

	}

	@Override
	public void bind(final ActivityLog activityLog) {

		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

}
