package com.busbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequest {
    private String name;
    private int age;
    private String gender;
    private String seatNumber;
}
