package acme.entities.recommendations;

import javax.validation.constraints.NotBlank;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String name;

	@NotBlank
	private String address;

	// Comma-separated list of categories from the API
	private String categories;

	// Could add more fields later based on API details (e.g., coordinates,
	// description, website)

}
