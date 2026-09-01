package com.busbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String busNumber;

    @Column(nullable = false)
    private String busName;

    @Enumerated(EnumType.STRING)
    private BusType busType;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private String operatorName;

    private Boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "route_id")
//    private Route route;
}
