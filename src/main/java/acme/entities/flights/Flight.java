
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
	@ValidString(min = 0, max = 50)
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

		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<String> originCity = repository.findOriginCity(this.getId(), pageRequest);
		return originCity.get(0);

	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<String> destinationCity = repository.findDestinationCity(this.getId(), pageRequest);
		return destinationCity.get(0);

	}

	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<Date> scheduledDeparture = repository.findDepartureTime(this.getId(), pageRequest);
		return scheduledDeparture.get(0);
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<Date> scheduledArrival = repository.findArrivalTime(this.getId(), pageRequest);
		return scheduledArrival.get(0);
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository;
		repository = SpringHelper.getBean(LegRepository.class);
		Integer legs = repository.findLayovers(this.getId());
		return legs;
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
