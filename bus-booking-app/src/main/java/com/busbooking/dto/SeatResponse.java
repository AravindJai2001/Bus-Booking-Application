package com.busbooking.dto;

import com.busbooking.entity.DeckType;
import com.busbooking.entity.SeatType;
import jakarta.validation.constraints.NotNull;

public record SeatResponse(
        Long id,
        String seatNumber,
        SeatType seatType,
        DeckType deckType,
        Integer rowNumber,
        Integer columnNumber,
        boolean windowSeat
){}
