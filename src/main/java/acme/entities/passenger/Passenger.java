
package acme.entities.passenger;

import java.util.Date;

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
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "customer_id"), @Index(columnList = "draftMode"), @Index(columnList = "customer_id, draftMode")
})
public class Passenger extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDate;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Optional
	@ValidString(max = 50, min = 0)
	@Automapped
	private String				specialNeeds;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;


	@Transient
	public String getDisplayString() {
		String fullName = this.getFullName();
		if (fullName != null && fullName.length() > 80)
			fullName = fullName.substring(0, 80) + "...";
		return String.format("%s - %s", fullName, this.getPassportNumber());
	}

}
