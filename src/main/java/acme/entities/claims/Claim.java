
package acme.entities.claims;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Optional
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	// Derived properties


	@Transient
	public String getStatus() {
		TrackingLogRepository repository;

		repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> allOrdered = repository.findAllOrderedByIndex(this.getId());
		return allOrdered.get(0).getStatus().toString();
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent registeredBy;

}
