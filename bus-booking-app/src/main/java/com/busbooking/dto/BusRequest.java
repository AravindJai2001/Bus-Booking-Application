package com.busbooking.dto;

import com.busbooking.entity.BusType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusRequest {

    @NotBlank
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$",
            message = "Invalid Bus Number"
    )
    private String busNumber;

    @NotBlank
    @Size(min = 3, max = 50)
    private String busName;

    @NotNull
    private BusType busType;

    @Min(10)
    @Max(60)
    private Integer totalSeats;

    @NotBlank
    private String operatorName;
}
