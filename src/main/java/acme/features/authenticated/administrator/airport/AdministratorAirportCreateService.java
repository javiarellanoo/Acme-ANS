
package acme.features.authenticated.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.airports.OperationalScope;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();
		airport.setName("");
		airport.setIataCode("");
		airport.setOperationalScope(OperationalScope.DOMESTIC);
		airport.setCity("");
		airport.setCountry("");
		airport.setWebsite("");
		airport.setEmail("");
		airport.setPhoneNumber("");

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("operationalScopes", choices);

		super.getResponse().addData(dataset);
	}

}
