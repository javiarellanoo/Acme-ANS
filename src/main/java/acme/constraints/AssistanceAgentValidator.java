
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.reviews.ReviewRepository;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state

	private ReviewRepository repository;

	// ConstraintValidator interface


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean sameLetters = false;
			boolean uniqueEmployeeCode = false;
			String employeeCode = assistanceAgent.getEmployeeCode();
			if (employeeCode != null) {
				String initials = "";
				initials += assistanceAgent.getIdentity().getName().trim().charAt(0);
				initials += assistanceAgent.getIdentity().getSurname().trim().charAt(0);

				sameLetters = employeeCode.startsWith(initials);

				uniqueEmployeeCode = this.repository.findSameEmployeeCode(assistanceAgent.getEmployeeCode()).size() == 1;
			}

			super.state(context, sameLetters, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
			super.state(context, uniqueEmployeeCode, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
