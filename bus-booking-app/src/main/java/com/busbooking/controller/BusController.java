package com.busbooking.controller;

import com.busbooking.dto.BusRequest;
import com.busbooking.dto.BusResponse;
import com.busbooking.dto.SeatRequest;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Route;
import com.busbooking.entity.Seat;
import com.busbooking.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/buses")
public class BusController {

    @Autowired
    private BusService service;

//    @GetMapping("/search")
//    public ResponseEntity<List<BusResponse>> findBusesByRoute(@RequestParam String source, @RequestParam String destination, @RequestParam String date) {
//        return new ResponseEntity<>(service.findBusesByRoute(source, destination, LocalDate.parse(date)), HttpStatus.OK);
//    }

    @GetMapping("/")
    public ResponseEntity<List<BusResponse>> getAllBuses(){
        return ResponseEntity.ok(service.getAllBuses());
    }

    @PostMapping("/")
    public ResponseEntity<Bus> addBus(@Valid @RequestBody BusRequest request) {
        return new ResponseEntity<>(service.addBus(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusResponse> getBusById(@PathVariable Long id){
        return ResponseEntity.ok(service.getBusById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody BusRequest request) {
        return ResponseEntity.ok(service.updateBus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable Long id){
        return ResponseEntity.ok(service.deleteBus(id));
    }

//    @PostMapping("/addRoute")
//    public ResponseEntity<Route> addRoute(@RequestBody Route route){
//        return new ResponseEntity<>(service.addRoute(route), HttpStatus.CREATED);
//    }
}
