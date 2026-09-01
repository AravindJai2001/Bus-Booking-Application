package com.busbooking.repository;

import com.busbooking.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

//    @Query("SELECT b FROM Bus b JOIN b.route r WHERE r.source = :source AND r.destination = :destination")
//    List<Bus> findBusesByRoute(String source, String destination);
//
//    @Query("SELECT b.price FROM Bus b WHERE b.id =:id")
//    double findPriceById(Long id);

    boolean existsByBusNumber(String busNumber);
}
