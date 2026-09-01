package com.busbooking.controller;

import com.busbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/initiate")
    public ResponseEntity<String> initialPayment(@RequestParam Long bookingId, @RequestParam String paymentMethod){
        return new ResponseEntity<>(service.initiatePayment(bookingId, paymentMethod), HttpStatus.CREATED);
    }

    @PostMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam Long bookingId, @RequestParam String txnId){
        return new ResponseEntity<>(service.paymentSuccess(bookingId, txnId), HttpStatus.OK);
    }

    @PostMapping("/failed")
    public ResponseEntity<String> paymentFailed( @RequestParam Long bookingId){
        return new ResponseEntity<>(service.paymentFailed(bookingId), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
