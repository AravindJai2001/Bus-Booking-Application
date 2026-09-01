package com.busbooking.repository;

import com.busbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            SELECT p.seatNumber FROM Booking b JOIN b.passengers p
            WHERE b.id = :id
            """)
    Set<String> findSeatNumberById(Long id);

    @Query("""
            SELECT b FROM Booking b
            LEFT JOIN FETCH b.passengers p
            WHERE b.userId = :userId
            ORDER BY b.journeyDate DESC
            """)
    List<Booking> findBookingsWithPassengersByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Booking b WHERE b.status = 'PAYMENT_PENDING' AND b.bookedTime <= :expiredTime")
    void deletePendingBookings(LocalTime expiredTime);
}
