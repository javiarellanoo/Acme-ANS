
package acme.features.authenticated.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Manager;

@GuiService
public class AuthenticatedManagerCreateService extends AbstractGuiService<Authenticated, Manager> {

	@Autowired
	private AuthenticatedManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !this.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager manager;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = this.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		manager = new Manager();
		manager.setUserAccount(userAccount);

		super.getBuffer().addData(manager);
	}

	@Override
	public void bind(final Manager manager) {
		super.bindObject(manager, "identifier", "yearsOfExperience", "dateOfBirth", "profilePicture");
	}

	@Override
	public void validate(final Manager manager) {
	}

	@Override
	public void perform(final Manager manager) {

		this.repository.save(manager);
	}

	@Override
	public void unbind(final Manager manager) {
		Dataset dataset;

		dataset = super.unbindObject(manager, "identifier", "yearsOfExperience", "dateOfBirth", "profilePicture");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
