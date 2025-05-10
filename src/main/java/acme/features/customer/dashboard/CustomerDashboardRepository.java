
package acme.features.customer.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.flights.Flight;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b.flight from Booking b where b.customer.id = :customerId and b.draftMode = false order by b.purchaseMoment desc")
	Collection<Flight> getFlightsOrderByRecentBooking(Integer customerId);

	@Query("select b from Booking b where b.customer.id = :customerId and b.draftMode = false")
	Collection<Booking> findBookings(int customerId);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId and b.draftMode = false group by b.travelClass")
	Collection<Object[]> findBookingsGroupedByTravelClass(int customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = false")
	Collection<Passenger> findPassengers(int customerId);

	@Query("select count(br) from BookingRecord br where br.booking.customer.id = :customerId and br.booking.draftMode = false group by br.booking.id")
	Collection<Long> findBookingRecordCountsPerBooking(int customerId);
}
