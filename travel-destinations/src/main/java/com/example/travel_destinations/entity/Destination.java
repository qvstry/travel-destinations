package com.example.travel_destinations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

//Destination entity, many to many with trips

@Entity
@Table(name = "destinations")
public class Destination {

    //-------Columns----
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long destinationId;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    @Column(nullable = false)
    private String country;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    @Column(nullable = false)
    private String city;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;


    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;


    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private DestinationCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "best_season")
    private Season bestSeason;

    // --------relationship------
  //many to many with trips
    @ManyToMany(mappedBy = "destinations", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Trip> trips = new ArrayList<>();

    //--------constructors----------
    public Destination() {}
    public Destination(String country, String city, DestinationCategory category) {
        this.country = country;
        this.city = city;
        this.category = category;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public DestinationCategory getCategory() {
        return category;
    }

    public void setCategory(DestinationCategory category) {
        this.category = category;
    }
    public Season getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(Season bestSeason) {
        this.bestSeason = bestSeason;
    }
    public List<Trip> getTrips() {
        return trips;
    }
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    //enum
    public enum DestinationCategory {
        BEACH,
        MOUNTAINS,
        CITY,
        CULTURE,
        ADVENTURE,
        NATURE,
        HISTORICAL
    }
    //enum season
    public enum Season {
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER
    }
}