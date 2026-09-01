package com.busbooking.service;

import com.busbooking.dto.BusRequest;
import com.busbooking.dto.BusResponse;
import com.busbooking.entity.Bus;
import com.busbooking.entity.Route;

import java.time.LocalDate;
import java.util.List;

public interface BusService {

//    List<BusResponse> findBusesByRoute(String source, String destination, LocalDate date);

    Bus addBus(BusRequest request);

//    Route addRoute(Route route);

    List<BusResponse> getAllBuses();

    BusResponse getBusById(Long id);

    Bus updateBus(Long id, BusRequest request);

    String deleteBus(Long id);
}
