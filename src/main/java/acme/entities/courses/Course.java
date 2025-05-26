
package acme.entities.courses;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				title;

	@Mandatory
	@ValidNumber(min = 0, max = 300)
	@Automapped
	private Integer				editionCount;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				firstPublishYear;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				authors;

}
