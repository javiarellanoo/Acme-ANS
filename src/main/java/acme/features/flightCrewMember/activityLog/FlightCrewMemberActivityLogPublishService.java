
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int activityLogId;
		ActivityLog log;
		FlightAssignment assignment;
		int memberId;

		activityLogId = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(activityLogId);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (log == null)
			status = false;
		else {
			assignment = log.getAssignment();
			if (!log.getDraftMode() || assignment.getFlightCrewMember().getId() != memberId)
				status = false;
			else if (super.getRequest().getMethod().equals("GET"))
				status = true;
			else
				status = true;
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

		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean assignmentIsPublished;

		assignmentIsPublished = !activityLog.getAssignment().getDraftMode();

		super.state(assignmentIsPublished, "*", "acme.validation.activity-log.not-published-assignment.message");

	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("assignmentIsPublished", false);

		super.getResponse().addData(dataset);

	}

}
