package com.busbooking.dto;

import com.busbooking.entity.DeckType;
import com.busbooking.entity.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record SeatRequest(

        @NotBlank(message = "Seat number is required")
        @Pattern(regexp = "^[A-Z][0-9]+$", message = "Invalid seat number format")
        String seatNumber,

        @NotNull(message = "Seat type is required")
        SeatType seatType,

        @NotNull(message = "Deck type is required")
        DeckType deckType,

        @NotNull(message = "Row number is required")
        @Positive(message = "Row number must be a positive integer")
        Integer rowNumber,

        @NotNull(message = "Column number is required")
        @Positive(message = "Column number must be a positive integer")
        Integer columnNumber,

        @NotNull(message = "Window seat flag is required")
        boolean windowSeat
) {
}
