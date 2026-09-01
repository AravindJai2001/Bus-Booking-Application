package com.busbooking.dto;

import java.time.LocalDateTime;

public record SeatLock(
        Long userId,
        LocalDateTime lockedAt
) {
}
