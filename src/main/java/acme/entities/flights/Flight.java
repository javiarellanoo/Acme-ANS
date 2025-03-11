
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(min = 0, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;


	//Derived properties
	// Relations 
	@Transient
	public String getOriginCity() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		List<Leg> legs = repository.findAllLegsByFlightId(this.getId());

		String originCity = legs.get(0).getDepartureAirport().getCity();
		return originCity;

	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		List<Leg> legs = repository.findAllLegsByFlightId(this.getId());

		String destinationCity = legs.get(legs.size() - 1).getDestinationAirport().getCity();
		return destinationCity;

	}

	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		List<Leg> legs = repository.findAllLegsByFlightId(this.getId());

		Date scheduledDeparture = legs.get(0).getScheduledDeparture();
		return scheduledDeparture;
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		List<Leg> legs = repository.findAllLegsByFlightId(this.getId());

		Date scheduledArrival = legs.get(legs.size() - 1).getScheduledArrival();
		return scheduledArrival;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository;
		repository = SpringHelper.getBean(LegRepository.class);
		List<Leg> legs = repository.findAllLegsByFlightId(this.getId());
		return legs.size() - 1;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager manager;

}
