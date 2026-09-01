package com.busbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long busId;
    private LocalDate journeyDate;
    private double totalFare;
    private String status; // e.g., "CONFIRMED", "CANCELLED"
    private LocalDateTime bookedTime;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.PERSIST)
    private List<Passenger> passengers;
}
