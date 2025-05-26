
package acme.features.any.review;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.reviews.Review;

@GuiService
public class ReviewShowService extends AbstractGuiService<Any, Review> {

	// Internal state

	@Autowired
	private ReviewRepository repository;

	// Abstract GUI interface


	@Override
	public void authorise() {
		Review review;
		int id;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		review = this.repository.findReviewById(id);
		status = review != null;
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Review review;
		int id;

		id = super.getRequest().getData("id", int.class);
		review = this.repository.findReviewById(id);

		super.getBuffer().addData(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "postedAt", "subject", "description", "score", "recommended");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}

}
