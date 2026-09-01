package com.busbooking.repository;

import com.busbooking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Query("""
            SELECT p.seatNumber FROM Passenger p
            WHERE p.booking.busId = :busId
            AND p.booking.journeyDate = :journeyDate
            AND p.booking.status = 'CONFIRMED'
            """)
    Set<String> findBookedSeats(Long busId, LocalDate journeyDate);

    @Query("""
            SELECT p.seatNumber FROM Passenger p
            WHERE p.booking.id = :bookingId
            """)
    Set<String> findSeatNumbersByBookingId(Long bookingId);
}
