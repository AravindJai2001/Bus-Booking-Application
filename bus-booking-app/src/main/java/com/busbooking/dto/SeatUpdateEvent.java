package com.busbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatUpdateEvent {

    private Long busId;
    private LocalDate journeyDate;
    private String seatNumber;
    private String status;
    private Long userId;
}
