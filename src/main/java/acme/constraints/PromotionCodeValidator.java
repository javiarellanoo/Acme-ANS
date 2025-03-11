
package acme.constraints;

import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

	@Override
	protected void initialise(final ValidPromotionCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promotionCode, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;

		boolean validCode = true;

		if (promotionCode != null) {
			int fullYear = LocalDate.now().getYear();
			String year = Integer.toString(fullYear).substring(-2);
			String codeYear = promotionCode.substring(-2);

			validCode = year.equals(codeYear);
		}

		super.state(context, validCode, "promotionCode", "acme.validation.service.promotionCode.message");

		result = !super.hasErrors(context);

		return result;
	}
}
