
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.airlines.Airline;
import acme.entities.legs.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

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
		String originCity;
		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<String> cities = repository.findOriginCity(this.getId(), pageRequest);
		if (cities.isEmpty())
			originCity = "Not defined yet";
		else
			originCity = cities.get(0);

		return originCity;

	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository;
		String destinationCity;
		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<String> cities = repository.findDestinationCity(this.getId(), pageRequest);
		if (cities.isEmpty())
			destinationCity = "Not defined yet";
		else
			destinationCity = cities.get(0);
		return destinationCity;

	}

	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository;
		Date scheduled = null;
		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<Date> departures = repository.findDepartureTime(this.getId(), pageRequest);
		if (!departures.isEmpty())
			scheduled = departures.get(0);
		return scheduled;
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository;
		Date scheduled = null;
		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<Date> arrivals = repository.findArrivalTime(this.getId(), pageRequest);
		if (!arrivals.isEmpty())
			scheduled = arrivals.get(0);
		return scheduled;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository;
		repository = SpringHelper.getBean(LegRepository.class);
		Integer legs = repository.findLayovers(this.getId());
		if (legs == -1)
			legs = 0;
		return legs;
	}

	@Transient
	public String getDisplayString() {
		return String.format("%s → %s", this.getOriginCity(), this.getDestinationCity());
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager	manager;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline	airline;

}
