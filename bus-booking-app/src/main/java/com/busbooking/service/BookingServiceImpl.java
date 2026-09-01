package com.busbooking.service;

import com.busbooking.dto.BookingRequest;
import com.busbooking.dto.BookingResponse;
import com.busbooking.dto.PassengerRequest;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Passenger;
import com.busbooking.mapper.BusMapper;
import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.PassengerRepository;
import com.busbooking.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @Autowired
    private SeatLockService seatLockService;

    @Autowired
    private BusMapper busMapper;

        @Transactional
        public String createBooking(BookingRequest request) {

            Set<String> bookedSeats = passengerRepo.findBookedSeats(request.getBusId(), request.getJourneyDate());

            System.out.println("BookedSeats : " + bookedSeats);

            for(String seat : request.getSeatNumbers()) {

                boolean seatLock = seatLockService.isLockValidForUser(request.getBusId(), request.getJourneyDate(), seat);

                if (!seatLock) {
                    throw new RuntimeException("Seat lock has expired or belongs to someone else.");
                }

                if (bookedSeats.contains(seat)) {
                    throw new RuntimeException("Seat " + seat + " is already Booked");
                }
            }

            Booking booking = new Booking();
            booking.setUserId(request.getUserId());
            booking.setBusId(request.getBusId());
            booking.setJourneyDate(request.getJourneyDate());
//            booking.setTotalFare(calculateTotalFare(request.getBusId(), request.getPassengers()));
            booking.setTotalFare(1000);
            booking.setStatus("PAYMENT_PENDING");
            booking.setBookedTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            ArrayList<Passenger> passengers = new ArrayList<>();

            for(PassengerRequest p : request.getPassengers()) {
                Passenger passenger = new Passenger(
                        null,
                        p.getName(),
                        p.getAge(),
                        p.getGender(),
                        p.getSeatNumber(),
                        booking
                );
                passengers.add(passenger);
            }
            booking.setPassengers(passengers);

            bookingRepository.save(booking);
            return "Booking created Successfully but payment is pending";
        }

    @Override
    public List<BookingResponse> getBookings() {
            Long userId = SecurityUtils.getCurrentUserId();
            if(userId == null){
                throw new RuntimeException("User not authenticated");
            }
            System.out.println("UserId for current User : " + userId);
            List<Booking> bookings = bookingRepository.findBookingsWithPassengersByUserId(userId);

            bookings.forEach(b -> System.out.println("Entity ID: " + b.getId()));

        return bookings.stream()
                .map(booking -> busMapper.toBookingResponse(booking))
                .toList();
    }

//    private double calculateTotalFare(Long busId, List<PassengerRequest> passengers) {
//            double seatPrice = busRepo.findPriceById(busId);
//            double total = seatPrice * passengers.size() + 30;
//            System.out.println("Seat Price " + seatPrice);
//            System.out.println("Total Price " + total);
//            return total;
//    }
}

