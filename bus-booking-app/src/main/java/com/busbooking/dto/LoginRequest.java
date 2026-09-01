package com.busbooking.dto;

public record LoginRequest(
        String emailId,
        String password
) {
}
