package com.busbooking.service;

import com.busbooking.dto.SeatLock;
import com.busbooking.dto.SeatLockRequest;

import java.time.LocalDate;
import java.util.Set;

public interface SeatLockService {
    boolean seatLock (SeatLockRequest request);

    void unlockSeat(long busId, LocalDate date, Set<String> seatNumber);

    boolean extendLock(SeatLockRequest request);

    boolean isLockValidForUser(Long busId, LocalDate date, String seatNumber);

    Set<String> getActiveLockedSeats(Long busId, LocalDate date);
}
