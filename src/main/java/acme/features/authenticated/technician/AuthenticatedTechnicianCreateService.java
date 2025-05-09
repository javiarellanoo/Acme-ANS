
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Technician;

@GuiService
public class AuthenticatedTechnicianCreateService extends AbstractGuiService<Authenticated, Technician> {

	@Autowired
	public AuthenticatedTechnicianRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !this.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician technician;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = this.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		technician = new Technician();
		technician.setUserAccount(userAccount);

		super.getBuffer().addData(technician);
	}

	@Override
	public void bind(final Technician technician) {
		super.bindObject(technician, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "experienceYears", "certifications");
	}

	@Override
	public void validate(final Technician technician) {
		;
	}

	@Override
	public void perform(final Technician technician) {
		this.repository.save(technician);
	}

	@Override
	public void unbind(final Technician technician) {
		Dataset dataset;

		dataset = super.unbindObject(technician, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "experienceYears", "certifications");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
