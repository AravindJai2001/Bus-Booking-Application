package com.busbooking.controller;

import com.busbooking.dto.SeatLockRequest;
import com.busbooking.dto.SeatRequest;
import com.busbooking.dto.SeatResponse;
import com.busbooking.entity.Seat;
import com.busbooking.service.SeatLockService;
import com.busbooking.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
public class SeatController {
    @Autowired
    private SeatService service;

    @Autowired
    private SeatLockService seatLockService;

    @GetMapping("/api/seats/{busId}")
    public ResponseEntity<List<SeatResponse>> findSeatsByBusId(@PathVariable long busId, @RequestParam String date) {
        return new ResponseEntity<>(service.findSeatsByBusId(busId, LocalDate.parse(date)), HttpStatus.OK);
    }

    @PostMapping("api/admin/buses/{busId}/seats")
    public ResponseEntity<List<Seat>> addSeat(@PathVariable Long busId, @RequestBody List<@Valid SeatRequest> request) {
        return new ResponseEntity<>(service.addSeat(busId, request), HttpStatus.CREATED);
    }

    @PutMapping("api/admin/seats/{seatId}")
    public ResponseEntity<Seat> addSeat(@PathVariable Long seatId, @RequestBody @Valid SeatRequest request) {
        return new ResponseEntity<>(service.updateSeat(seatId, request), HttpStatus.OK);
    }

    @PostMapping("/api/seats/lockSeats")
    public ResponseEntity<String> lockSeats(@RequestBody SeatLockRequest request) {
        boolean locked = seatLockService.seatLock(request);

        if(!locked){
            return ResponseEntity.badRequest().body("Seat already locked or it may be booked");
        }
        return ResponseEntity.ok("Seat Locked successfully");
    }

    @PostMapping("/api/seats/extendLock")
    public ResponseEntity<String> extendLock(@RequestBody SeatLockRequest request){
        boolean locked = seatLockService.extendLock(request);

        if(!locked){
            return ResponseEntity.badRequest().body("Seat lock extension failed. Locked seat belongs to someone else or Seat Lock expired.");
        }
        return ResponseEntity.ok("Seat lock extended successfully");
    }

}
