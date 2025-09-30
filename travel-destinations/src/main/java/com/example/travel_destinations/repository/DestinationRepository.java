package com.example.travel_destinations.repository;

import com.example.travel_destinations.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

//по країні
    List<Destination> findByCountryIgnoreCase(String country);

    //по категорії
    List<Destination> findByCategory(Destination.DestinationCategory category);

    //по сезону
    List<Destination> findByBestSeason(Destination.Season bestSeason);

    //по рейтингу
    List<Destination> findByRatingGreaterThanEqual(Double rating);

    //по категорії з рейтингом
    @Query("SELECT d FROM Destination d WHERE d.rating >= :minRating AND d.category = :category")
    List<Destination> findByCategoryAndMinRating(@Param("category") Destination.DestinationCategory category, @Param("minRating") Double minRating);

    //топ рейтинга
    @Query("SELECT d FROM Destination d ORDER BY d.rating DESC")
    List<Destination> findTopRatedDestinations();

    // рейтинг в діапазоні
    List<Destination> findByRatingBetween(Double minRating, Double maxRating);

    // destinations в країні
     long countByCountry(String country);

    // топ 5 destinations
     List<Destination> findTop5ByOrderByRatingDesc();}
