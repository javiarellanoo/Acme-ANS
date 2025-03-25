
package acme.entities.bookings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select count(b) from Booking b where locatorCode = :locatorCode")
	Long countSameLocatorCode(String locatorCode);

}
