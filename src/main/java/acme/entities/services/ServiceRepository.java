
package acme.entities.services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.promotionCode = ?1")
	Service findServiceByPromotionCode(String promotionCode);
}
