package com.busbooking.dto;

public record BusResponse(
        Long id,
        String busNumber,
        String busName,
        String busType,
        Integer totalSeats,
        String operatorName,
        Boolean active
) {
}
