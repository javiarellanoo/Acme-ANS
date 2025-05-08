package acme.entities.recommendation;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String name;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String city;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String state;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String formatted;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String openingHours;

	@Mandatory
	@ValidUrl
	@Automapped
	private String url;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String country;

}
