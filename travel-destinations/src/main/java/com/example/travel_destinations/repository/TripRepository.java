package com.example.travel_destinations.repository;

import com.example.travel_destinations.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    //всі подорожі тревелера
    List<Trip> findByTravelerTravelerId(Long travelerId);

    //по статусу
    List<Trip> findByStatus(Trip.TripStatus status);
    //по датам
    List<Trip> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    //по бюджету
    List<Trip> findByBudgetLessThanEqual(BigDecimal maxBudget);

    //trips по destination
    @Query("SELECT t FROM Trip t JOIN t.destinations d WHERE d.destinationId = :destinationId")
    List<Trip> findTripsToDestination(@Param("destinationId") Long destinationId);
    //trips по тревелер
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.traveler.travelerId = :travelerId")
    long countTripsByTraveler(@Param("travelerId") Long travelerId);

    //trips з бюджетом в діапазоні
     List<Trip> findByBudgetBetween(BigDecimal minBudget, BigDecimal maxBudget);


}