
package acme.features.technician.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.courses.Course;
import acme.realms.Technician;

@GuiController
public class TechnicianCourseController extends AbstractGuiController<Technician, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianCourseListService	listService;

	@Autowired
	private TechnicianCourseShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
