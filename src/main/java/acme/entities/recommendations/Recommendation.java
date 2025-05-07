package acme.entities.recommendations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String title;

	@NotBlank
	private String description;

	@NotBlank
	private String category;

	@NotBlank
	private String location;

	@NotNull
	private String city;

}
