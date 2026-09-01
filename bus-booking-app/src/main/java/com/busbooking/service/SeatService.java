package com.busbooking.service;

import com.busbooking.dto.SeatRequest;
import com.busbooking.entity.Seat;
import com.busbooking.dto.SeatResponse;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {
    List<SeatResponse> findSeatsByBusId(Long busId, LocalDate date);

    List<Seat> addSeat(Long busId, List<@Valid SeatRequest> request);

    Seat updateSeat(Long seatId, @Valid SeatRequest request);
}
