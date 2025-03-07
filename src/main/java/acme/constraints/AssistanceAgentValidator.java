
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.assistanceAgents.AssistanceAgent;
import acme.entities.assistanceAgents.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state

	@Autowired
	private AssistanceAgentRepository repository;

	// ConstraintValidator interface


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent value, final ConstraintValidatorContext context) {
		String employeeCode = value.getEmployeeCode();
		String fullName = value.getIdentity().getFullName();

		String[] words = fullName.trim().split("\\s+");

		StringBuilder initials = new StringBuilder();

		for (String word : words)
			if (!word.isEmpty())
				initials.append(word.charAt(0));

		String prefix = employeeCode.substring(0, initials.length());

		return initials.toString().toUpperCase().equals(prefix);
	}

}
