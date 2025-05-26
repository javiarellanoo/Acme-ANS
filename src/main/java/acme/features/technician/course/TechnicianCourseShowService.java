
package acme.features.technician.course;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.courses.Course;
import acme.realms.Technician;

@GuiService
public class TechnicianCourseShowService extends AbstractGuiService<Technician, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianCourseRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("id", int.class);
		course = this.repository.findCourseById(courseId);

		status = course != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course course;
		int id;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findCourseById(id);

		super.getBuffer().addData(course);

	}

	@Override
	public void unbind(final Course course) {
		Dataset dataset;

		dataset = super.unbindObject(course, "title", "editionCount", "firstPublishYear", "authors");

		super.getResponse().addData(dataset);
	}
}
