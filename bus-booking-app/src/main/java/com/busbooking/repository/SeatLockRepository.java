package com.busbooking.repository;

import com.busbooking.entity.SeatLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLock, Long> {

    List<SeatLock> findByBusIdAndJourneyDate(Long busId, LocalDate journeyDate);

    void deleteByBusIdAndJourneyDateAndSeatNumber(Long busId, LocalDate journeyDate, String seatNumber);

    @Modifying
    @Query("""
            DELETE FROM SeatLock sl
            WHERE sl.lockTime < :expiryTime
            """)
    void deleteExpiredLocks(LocalTime expiryTime);


}
