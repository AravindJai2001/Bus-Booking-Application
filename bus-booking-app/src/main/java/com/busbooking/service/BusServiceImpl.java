package com.busbooking.service;

import com.busbooking.dto.BusRequest;
import com.busbooking.dto.BusResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Route;
import com.busbooking.exception.BusinessException;
import com.busbooking.exception.ResourceNotFoundException;
import com.busbooking.mapper.BusMapper;
import com.busbooking.repository.BusRepository;
import com.busbooking.repository.PassengerRepository;
import com.busbooking.repository.RouteRepository;
import com.busbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService{

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private RouteRepository routeRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private BusMapper busMapper;

//    @Override
//    public List<BusResponse> findBusesByRoute(String source, String destination, LocalDate date) {
//        List<Bus> buses= busRepo.findBusesByRoute(source, destination);
//        return buses.stream()
//                .map(bus -> busMapper.toBusResponse(bus, passengerRepo.findBookedSeats(bus.getId(), date), seatRepo.findByBusId(bus.getId())))
//                .toList();
//    }

    @Override
    public Bus addBus(BusRequest request) {
        if(busRepo.existsByBusNumber(request.getBusNumber())){
            throw new BusinessException("Bus Number already exists");
        }
        Bus bus = new Bus(
                null,
                request.getBusNumber(),
                request.getBusName(),
                request.getBusType(),
                request.getTotalSeats(),
                request.getOperatorName(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return busRepo.save(bus);
    }

//    @Override
//    public Bus addBus(Bus bus) {
//        Route route = routeRepo.findById(bus.getRoute().getId()).orElseThrow(() -> new RuntimeException("Route not found"));
//        bus.setRoute(route);
//        return busRepo.save(bus);
//    }


//    public Route addRoute(Route route) {
//
//        return routeRepo.save(route);
//    }

    @Override
    public List<BusResponse> getAllBuses() {
        return busRepo.findAll().stream()
                .map(bus -> busMapper.toBusResponse(bus))
                .collect(Collectors.toList());
    }

    @Override
    public BusResponse getBusById(Long id) {
        Bus bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus not Found for the id : " + id));
        return busMapper.toBusResponse(bus);
    }

    @Override
    public Bus updateBus(Long id, BusRequest request) {
        Bus bus = busRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bus not Found for the id : " + id));
        bus.setBusNumber(request.getBusNumber());
        bus.setBusName(request.getBusName());
        bus.setBusType(request.getBusType());
        bus.setTotalSeats(request.getTotalSeats());
        bus.setOperatorName(request.getOperatorName());
        bus.setUpdatedAt(LocalDateTime.now());
        return null;
    }

    @Override
    public String deleteBus(Long id) {
        if(busRepo.existsById(id)) {
            busRepo.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Bus not found for the id : " + id);
        }
        return "Bus deleted Successfully";
    }
}
