
package acme.realms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select f from FlightCrewMember f where f.employeeCode = :employeeCode")
	FlightCrewMember findCrewMemberByEmployeeCode(String employeeCode);

}
