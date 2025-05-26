
package acme.entities.claims;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode")
})
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived properties


	@Transient
	public String getStatus() {
		TrackingLogRepository repository;

		repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> allTrackingLogs = repository.findAllByClaimId(this.getId());
		if (!allTrackingLogs.isEmpty())
			if (allTrackingLogs.stream().anyMatch(t -> t.getStatus().equals(TrackingLogStatus.DISSATISFACTION)))
				return TrackingLogStatus.DISSATISFACTION.toString();
			else {
				allTrackingLogs.sort(Comparator.comparing(TrackingLog::getCreationMoment).thenComparing(TrackingLog::getResolutionPercentage).reversed());
				return allTrackingLogs.get(0).getStatus().toString();
			}
		return "PENDING";
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;

}
