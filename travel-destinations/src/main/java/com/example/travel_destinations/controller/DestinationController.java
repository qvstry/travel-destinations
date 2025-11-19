package com.example.travel_destinations.controller;

import com.example.travel_destinations.entity.Destination;
import com.example.travel_destinations.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//REST Controller для Destinations
@RestController
@RequestMapping("/api/destinations")
@Tag(name = "Destinations", description = "API для управління Destinations")
public class DestinationController {

    @Autowired
    private DestinationRepository destinationRepository;
//public
    //отримати всі destinations
    @GetMapping("/public/all")
    @Operation(summary = "Отримати всі destinations")
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    // отримати destination по ID
    @GetMapping("/public/{id}")
    @Operation(summary = "Отримати destination по ID")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        Optional<Destination> destination = destinationRepository.findById(id);
        return destination.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //manager
    // Count
    @GetMapping("/manager/count")
    @Operation(summary = "Отримати кількість destinations")
    public ResponseEntity<Long> getDestinationsCount() {
        long count = destinationRepository.count();
        return ResponseEntity.ok(count);
    }
    //по країні
    @GetMapping("/manager/country/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Отримати destinations по країні")
    public List<Destination> getDestinationsByCountry(@PathVariable String country) {
        return destinationRepository.findByCountryIgnoreCase(country);
    }

    //по категорії
    @GetMapping("/public/category/{category}")
    @Operation(summary = "Отримати destinations по категорії")
    public List<Destination> getDestinationsByCategory(@PathVariable Destination.DestinationCategory category) {
        return destinationRepository.findByCategory(category);
    }

    //по найкращому season
    @GetMapping("/public/{season}")
    @Operation(summary = "Отримати destination по сезону")
    public List<Destination> getDestinationsBySeason(@PathVariable Destination.Season season) {
        return destinationRepository.findByBestSeason(season);
    }

    //по рейтингу
    @GetMapping("/rating/{minRating}")
    @Operation(summary = "Отримати destinations з рейтингом від та вище за")
    public List<Destination> getDestinationsByMinRating(@PathVariable Double minRating) {
        return destinationRepository.findByRatingGreaterThanEqual(minRating);
    }

    //top-rated
    @GetMapping("/top-rated")
    @Operation(summary = "Отримати top-rated destinations")
    public List<Destination> getTopRatedDestinations() {
        return destinationRepository.findTopRatedDestinations();
    }

    //фільтр по категорії та мін рейтингу
    @GetMapping("/filter")
    @Operation(summary = "Фільтр по категорії та рейтингу")
    public List<Destination> filterDestinations(@RequestParam Destination.DestinationCategory category, @RequestParam Double minRating) {
        return destinationRepository.findByCategoryAndMinRating(category, minRating);
    }

    // нове destination
    @PostMapping("/manager/create")
    @Operation(summary = "Створити нове destination")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Destination> createDestination(@Valid @RequestBody Destination destination) {
        Destination savedDestination = destinationRepository.save(destination);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDestination);
    }

    //Put
    @PutMapping("/manager/update/{id}")
    @Operation(summary = "Повністю оновити destination")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Destination> updateDestination(@PathVariable Long id,  @Valid @RequestBody Destination destinationDetails) {
        return destinationRepository.findById(id)
                .map(destination -> {
                    destination.setCountry(destinationDetails.getCountry());
                    destination.setCity(destinationDetails.getCity());
                    destination.setDescription(destinationDetails.getDescription());
                    destination.setRating(destinationDetails.getRating());
                    destination.setCategory(destinationDetails.getCategory());
                    destination.setBestSeason(destinationDetails.getBestSeason());

                    return ResponseEntity.ok(destinationRepository.save(destination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Patch rating
    @PatchMapping("/{id}/rating")
    @Operation(summary = "Оновити рейтинг destination")
    public ResponseEntity<Destination> updateDestinationRating(@PathVariable Long id, @RequestParam Double rating) {
        // чекаємо рейтинг
        if (rating < 1.0 || rating > 5.0) {
            return ResponseEntity.badRequest().build();
        }

        return destinationRepository.findById(id)
                .map(destination -> {
                    destination.setRating(rating);
                    return ResponseEntity.ok(destinationRepository.save(destination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Patch description
    @PatchMapping("/{id}/description")
    @Operation(summary = "Оновити опис destination")
    public ResponseEntity<Destination> updateDestinationDescription(@PathVariable Long id, @RequestBody String description) {
        return destinationRepository.findById(id)
                .map(destination -> {
                    destination.setDescription(description);
                    return ResponseEntity.ok(destinationRepository.save(destination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //patch season
    @PatchMapping("/{id}/season")
    @Operation(summary = "Оновити найкращий сезон")
    public ResponseEntity<Destination> updateDestinationSeason(@PathVariable Long id, @RequestParam Destination.Season season) {
        return destinationRepository.findById(id)
                .map(destination -> {
                    destination.setBestSeason(season);
                    return ResponseEntity.ok(destinationRepository.save(destination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/admin/delete/{id}")
   //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити destination")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        return destinationRepository.findById(id)
                .map(destination -> {
                    destinationRepository.delete(destination);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }




}