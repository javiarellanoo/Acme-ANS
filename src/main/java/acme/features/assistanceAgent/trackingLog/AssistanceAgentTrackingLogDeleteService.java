
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	//AbstractGuiService
	@Override
	public void authorise() {
		boolean status;

		if (super.getRequest().getMethod().equals("GET"))
			status = false;
		else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		tLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(tLog);
	}

	@Override
	public void bind(final TrackingLog tLog) {
		super.bindObject(tLog, "stepUndergoing", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog tLog) {

	}

	@Override
	public void perform(final TrackingLog tLog) {
		this.repository.delete(tLog);
	}
}
