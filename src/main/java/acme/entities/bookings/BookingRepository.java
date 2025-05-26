
package acme.entities.bookings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking getBookingByLocatorCode(String locatorCode);

	@Query("select count(br) from BookingRecord br where br.booking.id = :bookingId")
	Long countNumberOfPassengers(int bookingId);

	@Query("select br from BookingRecord br where br.passenger.id = :passengerId and br.booking.id = :bookingId")
	BookingRecord findBookingRecordByPassengerAndBooking(int passengerId, int bookingId);
}
