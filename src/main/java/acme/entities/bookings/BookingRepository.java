
package acme.entities.bookings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where where locatorCode = :locatorCode")
	Booking getBookingByLocatorCode(String locatorCode);

	@Query("select count(b) from Booking b where locatorCode = :locatorCode")
	Long countSameLocatorCode(String locatorCode);

}
