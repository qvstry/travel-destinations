package com.example.travel_destinations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Trip entity, many to one with Traveler, many to many with destination
 * columns:
 * trip_id
 * name
 * start_date
 * end_date
 * budget
 * status
 * traveler_id
 */
@Entity
@Table(name = "trips")
public class Trip {

    // --------Columns------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @NotBlank(message = "Trip name is required")
    @Size(min = 2, max = 120, message = "Trip name must be between 2 and 120 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be positive")
    @Column(nullable = false, precision = 10, scale = 2) //загальна кількість цифр
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TripStatus status = TripStatus.PLANNED;

    // -----relationship-----

    //many to many trip destination, join table trip_id, destination id
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "trip_destinations",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id")
    )
    private List<Destination> destinations = new ArrayList<>();
//many to one with traveler
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traveler_id", referencedColumnName = "traveler_id")
    @JsonIgnore
    private Traveler traveler;

    // ----constructors-----
    public Trip() {}
    public Trip(String name, LocalDate startDate, LocalDate endDate, BigDecimal budget) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = TripStatus.PLANNED;
    }

    // ------Gettter ans setters --------

    public Long getTripId() {
        return tripId;
    }
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }
    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public Traveler getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }
    // enum
    public enum TripStatus {
        PLANNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}