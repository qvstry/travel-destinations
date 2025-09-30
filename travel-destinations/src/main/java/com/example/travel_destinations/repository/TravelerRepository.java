package com.example.travel_destinations.repository;

import com.example.travel_destinations.entity.Traveler;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelerRepository extends JpaRepository<Traveler, Long> {

    //по email
    Optional<Traveler> findByEmail(String email);
    // ім'я
    List<Traveler> findTravelerByName(String name);

    // fav destination
    @Query("SELECT t FROM Traveler t WHERE t.favoriteDestinationId = :destinationId")
    List<Traveler> findByFavoriteDestination(@Param("destinationId") Long destinationId);

// перевірка імейлу
    boolean existsByEmail(String email);

    //по стилю
    List<Traveler> findByTravelStyle(Traveler.TravelStyle travelStyle);

}