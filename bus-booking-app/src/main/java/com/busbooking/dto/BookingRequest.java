package com.busbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private Long userId;
    private Long busId;
    private LocalDate journeyDate;
    private List<String> seatNumbers;
    private List<PassengerRequest> passengers;

}
