
package acme.features.authenticated.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorBookingRecordRepository extends AbstractRepository {

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findPublishedBookings();

	@Query("select p from BookingRecord br join br.passenger p where br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);
	
	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int bookingId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select br from BookingRecord br where br.id = :id")
	BookingRecord findBookingRecordById(int id);
}
