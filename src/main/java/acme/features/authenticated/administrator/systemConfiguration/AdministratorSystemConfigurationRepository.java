
package acme.features.authenticated.administrator.systemConfiguration;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;

public interface AdministratorSystemConfigurationRepository extends AbstractRepository {

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfig();
}
