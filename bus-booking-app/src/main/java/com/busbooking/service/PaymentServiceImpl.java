package com.busbooking.service;

import com.busbooking.entity.Booking;
import com.busbooking.entity.Payment;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.PassengerRepository;
import com.busbooking.repository.PaymentRepository;
import com.busbooking.repository.SeatLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @Autowired
    private SeatLockService service;

    @Override
    @Transactional
    public String initiatePayment(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not foud for this BookingID : " + bookingId));
        if(!Objects.equals(booking.getStatus(), "PAYMENT_PENDING")){
            throw new RuntimeException("Invalid booking status");
        }
        Payment payment = new Payment(
                null,
                bookingId,
                booking.getTotalFare(),
                paymentMethod,
                "INITIATED",
                null
        );
        paymentRepo.save(payment);
        return "Payment Successfully Initiated";
    }

    @Transactional
    public String paymentSuccess(Long bookingId, String txnId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found for this BookingId : " + bookingId));
        if(!booking.getStatus().equals("PAYMENT_PENDING")){
            throw new RuntimeException("Invalid Booking status");
        }

        booking.setStatus("CONFIRMED");

        Payment payment = paymentRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not Found"));

        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionId(txnId);

        paymentRepo.save(payment);
        bookingRepo.save(booking);
        Set<String> lockedSeats = passengerRepo.findSeatNumbersByBookingId(booking.getId());
        System.out.println("Locked seats for this booking : " + lockedSeats);

        service.unlockSeat(booking.getBusId(), booking.getJourneyDate(), lockedSeats);

        System.out.println("Seat are successfully unlocked");

        return "Payment Successfully Completed";
    }

    @Transactional
    public String paymentFailed(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found for this BookingId : " + bookingId));
        Payment payment = paymentRepo.findByBookingId(bookingId).orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus("FAILURE");
        paymentRepo.save(payment);

        booking.setStatus("FAILED");
        bookingRepo.save(booking);

        Set<String> lockedSeats = passengerRepo.findSeatNumbersByBookingId(booking.getId());
        System.out.println("Locked seats for this failed booking : " + lockedSeats);

        service.unlockSeat(booking.getBusId(), booking.getJourneyDate(), lockedSeats);

        System.out.println("Seat are successfully unlocked");

        return "Payment Failed";
    }
}
