
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Passenger passenger;
		int passengerId;
		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		if (passenger != null && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer()))
			status = passenger.getDraftMode();
		else
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
		passenger.setCustomer(customer);
	}

	@Override
	public void validate(final Passenger passenger) {
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
