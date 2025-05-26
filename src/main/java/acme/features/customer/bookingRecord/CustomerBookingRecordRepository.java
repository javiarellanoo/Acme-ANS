
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select br from BookingRecord br where br.id = :id")
	BookingRecord findBookingRecordById(int id);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int bookingId);

	@Query("select p from Passenger p where p.customer.id = :id and p.draftMode = 0")
	Collection<Passenger> findAllMyPassengers(int id);

	@Query("SELECT p FROM Passenger p LEFT JOIN BookingRecord br ON br.passenger = p AND br.booking.id = :bookingId WHERE p.customer.id = :customerId AND br.id IS NULL and p.draftMode = 0")
	Collection<Passenger> findPassengersNotInBooking(int customerId, int bookingId);

	@Query("select p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("select br from BookingRecord br where br.passenger.id = :passengerId and br.booking.id = :bookingId")
	BookingRecord findBookingRecordByPassengerAndBooking(int passengerId, int bookingId);
}
