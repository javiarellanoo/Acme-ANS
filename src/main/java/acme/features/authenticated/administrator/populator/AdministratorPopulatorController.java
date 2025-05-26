
package acme.features.authenticated.administrator.populator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import acme.client.components.principals.Administrator;
import acme.client.controllers.GuiController;
import acme.client.helpers.Assert;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.StringHelper;

@GuiController
public class AdministratorPopulatorController {

	// Endpoints --------------------------------------------------------------

	@GetMapping("/administrator/system/populate-recommendations")
	public ModelAndView populateInitial() {
		Assert.state(PrincipalHelper.get().hasRealmOfType(Administrator.class), "acme.default.error.not-authorised");

		ModelAndView result;

		result = this.doPopulate("recommendation");

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView doPopulate(final String descriptor) {
		assert !StringHelper.isBlank(descriptor) && StringHelper.anyOf(descriptor, "none|initial|sample|recommendation");
		Assert.state(PrincipalHelper.get().hasRealmOfType(Administrator.class), "acme.default.error.not-authorised");

		ModelAndView result;

		try {
			CustomLauncher.reset(false, descriptor);
			PrincipalHelper.handleUpdate();
			result = new ModelAndView();
			result.setViewName("fragments/welcome");
			result.addObject("_globalSuccessMessage", "acme.default.global.message.success");
		} catch (final Throwable oops) {
			result = new ModelAndView();
			result.setViewName("master/panic");
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			result.addObject("_globalErrorMessage", "acme.default.global.message.error");
			result.addObject("_oops", oops);
		}

		return result;
	}

}
