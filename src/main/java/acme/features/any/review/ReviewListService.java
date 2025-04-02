
package acme.features.any.review;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.reviews.Review;

@GuiService
public class ReviewListService extends AbstractGuiService<Any, Review> {

	// Internal state

	@Autowired
	private ReviewRepository repository;

	// Abstract GUI interface


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Review> reviews;

		Date oneYearBeforeCurrentMoment = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);
		reviews = this.repository.findReviewsAfterDate(oneYearBeforeCurrentMoment);

		super.getBuffer().addData(reviews);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "postedAt", "subject", "description", "score", "recommended");

		super.getResponse().addData(dataset);
	}
}
