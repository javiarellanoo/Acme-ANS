
package acme.features.any.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.services.Service;

@ControllerAdvice
public class AnyServiceAdvisor {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyServiceRepository repository;

	// Beans ------------------------------------------------------------------


	@ModelAttribute("service")
	public Service getService() {
		Service result;

		try {
			result = this.repository.findRandomService();
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}

}
