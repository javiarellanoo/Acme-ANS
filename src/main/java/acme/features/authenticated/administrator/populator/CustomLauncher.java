
package acme.features.authenticated.administrator.populator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;

@SpringBootApplication
public class CustomLauncher {

	public static void reset(final boolean createSchema, final String descriptor) {
		assert !StringHelper.isBlank(descriptor) && StringHelper.anyOf(descriptor, "none|initial|sample|recommendation");

		CustomPopulator populator;

		MomentHelper.reset();
		RandomHelper.reset();

		populator = SpringHelper.getBean(CustomPopulator.class);
		populator.populate(createSchema, descriptor);

		MomentHelper.reset();
		RandomHelper.reset();
	}

}
