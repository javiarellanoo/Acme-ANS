
package acme.features.any.review;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.entities.reviews.Review;

@Controller
public class ReviewController extends AbstractGuiController<Any, Review> {

	// Internal state

	@Autowired
	private ReviewListService	listService;

	@Autowired
	private ReviewShowService	showService;

	@Autowired
	private ReviewCreateService	createService;

	// Constructors


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}

}
