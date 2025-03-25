
package acme.realms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT a FROM AssistanceAgent a WHERE a.employeeCode = :employeeCode")
	List<AssistanceAgent> findSameEmployeeCode(String employeeCode);

}
