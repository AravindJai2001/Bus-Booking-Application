package com.busbooking.service;

import com.busbooking.dto.SeatRequest;
import com.busbooking.dto.SeatResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Seat;

import com.busbooking.entity.SeatStatus;
import com.busbooking.exception.BusinessException;
import com.busbooking.exception.ResourceNotFoundException;
import com.busbooking.mapper.BusMapper;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.PassengerRepository;

import com.busbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Service
public class SeatServiceImpl implements SeatService{

    @Autowired
    private SeatRepository repo;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BusMapper seatMapper;

    @Autowired
    private SeatLockService seatLockService;

    @Override
    public List<SeatResponse> findSeatsByBusId(Long busId, LocalDate date) {
        List<Seat> seats = repo.findByBusId(busId);
        Set<String> bookedSeats = passengerRepository.findBookedSeats(busId, date);
        Set<String> lockedSeatNumbers = seatLockService.getActiveLockedSeats(busId, date);
        System.out.println("Booked Seats: " + bookedSeats);
        System.out.println("Locked Seats: " + lockedSeatNumbers);
         return seats.stream()
                 .map(seat -> seatMapper.toSeatResponse(seat, bookedSeats, lockedSeatNumbers))
                 .toList();
    }

    @Override
    @Transactional
    public List<Seat> addSeat(Long busId, List<SeatRequest> seatRequests) {
        Bus bus = busRepository.findById(busId).orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        if(!seatRequests.isEmpty() && seatRequests.size() == bus.getTotalSeats()) {
            List<Seat> seats = new ArrayList<>();
            for(SeatRequest request : seatRequests) {
                if(!repo.existsByBusIdAndSeatNumber(busId, request.seatNumber())) {
                    Seat seat = new Seat(
                            null,
                            request.seatNumber(),
                            request.seatType(),
                            request.deckType(),
                            request.rowNumber(),
                            request.columnNumber(),
                            request.windowSeat(),
                            SeatStatus.ACTIVE,
                            bus
                    );
                    seats.add(seat);
                }else{
                    throw new BusinessException("Seat number already exists for this bus : " + busId);
                }
            }
            System.out.println("Seats added successfully");
            return repo.saveAll(seats);
        }else{
            throw new BusinessException("Seat number already exists for this bus");
        }
    }

    @Override
    public Seat updateSeat(Long seatId, SeatRequest request) {
        Seat seat = repo.findById(seatId).orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
//        if(!repo.existsByBusIdAndSeatNumber(seat.getBus().getId(), request.seatNumber()))
        //update logic to update only minor changes
        //update to update the major changes
        seat.setSeatNumber(request.seatNumber());
        seat.setSeatType(request.seatType());
        seat.setDeckType(request.deckType());
        seat.setRowNumber(request.rowNumber());
        seat.setColumnNumber(request.columnNumber());
        seat.setWindowSeat(request.windowSeat());
        return null;
    }
}
