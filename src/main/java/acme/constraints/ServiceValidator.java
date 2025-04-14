
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.services.Service;
import acme.entities.services.ServiceRepository;

@Validator
public class ServiceValidator extends AbstractValidator<ValidService, Service> {

	@Autowired
	private ServiceRepository serviceRepository;


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;
		Boolean validCode = true;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (service.getPromotionCode() != null && service.getPromotionCode().length() >= 2) {
			String promotionCode = service.getPromotionCode();

			Integer fullYear = MomentHelper.getCurrentMoment().getYear();
			String year = fullYear.toString().substring(fullYear.toString().length() - 2);
			String codeYear = promotionCode.substring(promotionCode.length() - 2);

			validCode = year.equals(codeYear);

			Service alreadyExistingService = this.serviceRepository.findServiceByPromotionCode(promotionCode);

			validCode &= alreadyExistingService == null || alreadyExistingService.getId() == service.getId();
		}

		super.state(context, validCode, "promotionCode", "acme.validation.service.promotionCode.message");

		result = !super.hasErrors(context);
		return result;
	}
}
