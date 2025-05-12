
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingkById(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findAllBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("select br from BookingRecord br where br.id = :id")
	BookingRecord findBookingRecordById(int id);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int bookingId);

	@Query("select br.booking from BookingRecord br where br.id = :id")
	Booking findBookingOfBookingRecordById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f from Flight f where f.draftMode = 0")
	Collection<Flight> findAllNotDraftFlights();

	@Query("select br from BookingRecord br where br.booking.id = :id")
	Collection<BookingRecord> findRelationshipsByBooking(int id);

	@Query("select count(br) from BookingRecord br where br.booking.id = :bookingId")
	Long countNumberOfPassengers(int bookingId);

}
