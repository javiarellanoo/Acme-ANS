
package acme.features.authenticated.administrator.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.services.Service;

@Repository
public interface AdministratorServiceRepository extends AbstractRepository {

	@Query("select s from Service s")
	Collection<Service> findAllServices();

	@Query("select s from Service s where s.id = :id")
	Service findServiceById(int id);

}
