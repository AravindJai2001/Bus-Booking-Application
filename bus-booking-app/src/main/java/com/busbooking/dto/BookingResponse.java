package com.busbooking.dto;

import java.time.LocalDate;
import java.util.List;

public record BookingResponse(
        Long bookingId,
        Long busId,
        LocalDate journeyDate,
        String status,
        double totalFare,
        List<PassengerResponse> passengers
) {
}
