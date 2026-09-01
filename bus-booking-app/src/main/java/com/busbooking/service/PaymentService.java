package com.busbooking.service;

public interface PaymentService {

    String initiatePayment(Long bookingId, String paymentMethod);

    String paymentSuccess(Long bookingId, String txnId);

    String paymentFailed(Long bookingId);
}
