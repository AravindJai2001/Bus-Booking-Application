package com.busbooking.service;

import com.busbooking.dto.BookingRequest;
import com.busbooking.dto.BookingResponse;
import com.busbooking.entity.Booking;

import java.util.List;

public interface BookingService {

    String createBooking(BookingRequest request);

    List<BookingResponse> getBookings();
}
