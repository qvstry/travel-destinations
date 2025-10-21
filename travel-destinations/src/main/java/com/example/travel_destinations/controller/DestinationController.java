package com.example.travel_destinations.controller;

import com.example.travel_destinations.entity.Destination;
import com.example.travel_destinations.entity.Message;
import com.example.travel_destinations.repository.DestinationRepository;
import com.example.travel_destinations.service.DestinationService;
import com.example.travel_destinations.service.MessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private DestinationService destinationService;

    //отримати всі destinations
    @GetMapping
    @Operation(summary = "Отримати всі destinations")
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }
    // Count
    @GetMapping("/count")
    @Operation(summary = "Отримати кількість destinations")
    public ResponseEntity<Long> getDestinationsCount() {
        long count = destinationRepository.count();
        return ResponseEntity.ok(count);
    }
    // отримати destination по ID
    @GetMapping("/{id}")
    @Operation(summary = "Отримати destination по ID")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        Optional<Destination> destination = destinationRepository.findById(id);
        return destination.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //по країні
    @GetMapping("/country/{country}")
    @Operation(summary = "Отримати destinations по країні")
    public List<Destination> getDestinationsByCountry(@PathVariable String country) {
        return destinationRepository.findByCountryIgnoreCase(country);
    }

    //по категорії
    @GetMapping("/category/{category}")
    @Operation(summary = "Отримати destinations по категорії")
    public List<Destination> getDestinationsByCategory(@PathVariable Destination.DestinationCategory category) {
        return destinationRepository.findByCategory(category);
    }

    //по найкращому season
    @GetMapping("/season/{season}")
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
    // ------ Rabbit MQ---------
    @PostMapping
    @Operation(summary = "Створити нове destination")
    public ResponseEntity<Destination> createDestination(@Valid @RequestBody Destination destination) {
        Destination savedDestination = destinationService.createDestination(destination);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDestination);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити destination")
    public ResponseEntity<Destination> updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody Destination destinationDetails) {
        try {
            Destination updated = destinationService.updateDestination(id, destinationDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити destination")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        try {
            destinationService.deleteDestination(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

/*
    // нове destination
    @PostMapping
    @Operation(summary = "Створити нове destination")
    public ResponseEntity<Destination> createDestination(@Valid @RequestBody Destination destination) {
        Destination savedDestination = destinationRepository.save(destination);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDestination);
    }

    //Put
    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити destination")
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
*/
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
    @Autowired
    private MessageProducer messageProducer;

    // Endpoint для тесту Exception
    @PostMapping("/test-exception")
    @Operation(summary = "Тест Exception Dead Letter Queue")
    public ResponseEntity<String> testException() {
        System.out.println("\n Відправка повідомлення для Exception");

        Message errorMessage = new Message(
                999L,
                "Помилкові дані для тестування Exception handling",
                "ErrorCountry",
                "CREATE"  // RuntimeException
        );

        messageProducer.sendMessage(errorMessage);

        return ResponseEntity.ok("Exception test message відправлено!");
    }
    /* Delete
    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити destination")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        return destinationRepository.findById(id)
                .map(destination -> {
                    destinationRepository.delete(destination);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }*/




}