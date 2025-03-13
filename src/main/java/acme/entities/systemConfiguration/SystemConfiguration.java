
package acme.entities.systemConfiguration;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidCurrency;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidCurrency
	@Automapped
	private String				systemCurrency;

	@Mandatory
	@ValidString(pattern = "([A-Z]{3})(, [A-Z]{3})*", min = 1, max = 255)
	@Automapped
	private String				acceptedCurrencies;
}
