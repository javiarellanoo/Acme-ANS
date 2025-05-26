
package acme.features.technician.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.courses.Course;
import acme.realms.Technician;

@GuiService
public class TechnicianCourseListService extends AbstractGuiService<Technician, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianCourseRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Course> courses;

		courses = this.repository.findAllCourses();

		super.getBuffer().addData(courses);
	}

	@Override
	public void unbind(final Course course) {
		Dataset dataset;

		dataset = super.unbindObject(course, "title", "editionCount", "firstPublishYear", "authors");

		super.addPayload(dataset, course, "title", "editionCount", "firstPublishYear", "authors");
		super.getResponse().addData(dataset);
	}
}
