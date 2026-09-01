package com.busbooking.mapper;

import com.busbooking.dto.BookingResponse;
import com.busbooking.dto.BusResponse;
import com.busbooking.dto.SeatResponse;
import com.busbooking.entity.Booking;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BusMapper {

//    @Mapping(target = "duration", expression = "java(calculateDuration(bus.getDepartureTime(), bus.getArrivalTime()))")
//    @Mapping(target = "availableSeats", expression = "java(seats.size() - bookedSeats.size())")
    BusResponse toBusResponse(Bus bus);

//    default String calculateDuration(String departureTime, String arrivalTime) {
//        java.time.LocalTime departure = java.time.LocalTime.parse(departureTime);
//        java.time.LocalTime arrival = java.time.LocalTime.parse(arrivalTime);
//
//        java.time.Duration duration = java.time.Duration.between(departure, arrival);
//
//        if (duration.isNegative()) {
//            duration = duration.plusHours(24);
//        }
//
//        long hours = duration.toHours();
//        long minutes = duration.toMinutes() % 60;
//        return hours + "h " + minutes + "m";
//    }

    @Mapping(target = "status", expression = "java(getSeatStatus(seat.getSeatNumber(), bookedSeats, lockedSeats))")
    SeatResponse toSeatResponse(Seat seat, @Context Set<String> bookedSeats, Set<String> lockedSeats);

    default String getSeatStatus(String seatNumber, Set<String> bookedSeats, Set<String> lockedSeats) {
        String status = "AVAILABLE";
        if (bookedSeats.contains(seatNumber)) {
            status = "BOOKED";
        } else if (lockedSeats.contains(seatNumber)) {
            status = "LOCKED";
        }
        return status;
    }

    @Mapping(source = "id", target = "bookingId")
    BookingResponse toBookingResponse(Booking booking);

}
