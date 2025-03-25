
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;
import acme.realms.repositories.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state

	private AssistanceAgentRepository repository;

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
		else if (assistanceAgent.getEmployeeCode() != null) {
			String employeeCode = assistanceAgent.getEmployeeCode();

			Boolean sameLetters = false;
			boolean uniqueEmployeeCode = false;
			String initials = "";
			initials += assistanceAgent.getIdentity().getName().trim().charAt(0);
			initials += assistanceAgent.getIdentity().getSurname().trim().charAt(0);

			sameLetters = employeeCode.startsWith(initials);

			List<AssistanceAgent> existingEmployeeCode = this.repository.findSameEmployeeCode(assistanceAgent.getEmployeeCode());

			uniqueEmployeeCode = existingEmployeeCode.size() == 1 || existingEmployeeCode.isEmpty();

			super.state(context, sameLetters, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
			super.state(context, uniqueEmployeeCode, "employeeCode", "acme.validation.assistanceAgent.employeeCode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
