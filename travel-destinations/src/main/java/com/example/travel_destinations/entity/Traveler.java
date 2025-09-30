package com.example.travel_destinations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Traveler entity, one-to-many relationship with trips
 *  columns:
 * traveler id
 * name
 * email
 * phone
 * registration date
 * fav destination
 * travel style
 */
@Entity
@Table(name = "travelers")
public class Traveler {

    // --------Columns------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "traveler_id")
    private Long travelerId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 20, message = "Phone number too long")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_style")
    private TravelStyle travelStyle;

    @Column(name = "registration_date")
    private LocalDate registrationDate;


    @Column(name = "favorite_destination_id")
    private Long favoriteDestinationId;

    // -----relationship-----

    //One to Many with traveler
    @OneToMany(mappedBy = "traveler", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Trip> trips = new ArrayList<>();

    // ----constructors-----
    public Traveler() {
        this.registrationDate = LocalDate.now(); // cьогоднішня дата
        this.travelStyle = TravelStyle.SOLO;
    }

    public Traveler(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    // ------Gettter ans setters --------

    public Long getTravelerId() {
        return travelerId;
    }
    public void setTravelerId(Long travelerId) {
        this.travelerId = travelerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TravelStyle getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(TravelStyle travelStyle) {
        this.travelStyle = travelStyle;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getFavoriteDestinationId() {
        return favoriteDestinationId;
    }

    public void setFavoriteDestinationId(Long favoriteDestinationId) {
        this.favoriteDestinationId = favoriteDestinationId;
    }

    public List<Trip> getTrips() {
        return trips;
    }
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    // enum

    public enum TravelStyle {
        SOLO,
        FAMILY,
        GROUP,
        COUPLE
    }
}