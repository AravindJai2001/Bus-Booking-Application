package com.busbooking.controller;

import com.busbooking.dto.BookingRequest;
import com.busbooking.dto.BookingResponse;
import com.busbooking.security.JWTUtil;
import com.busbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JWTUtil jwtUtil;

     @PostMapping("/create")
     public ResponseEntity<String> createBooking(@RequestBody BookingRequest request) {
         return new ResponseEntity<>(bookingService.createBooking(request), HttpStatus.CREATED);
     }

     @GetMapping("/history")
    public  ResponseEntity<List<BookingResponse>> getBookings(){
         return new ResponseEntity<>(bookingService.getBookings(), HttpStatus.FOUND);
     }
}
