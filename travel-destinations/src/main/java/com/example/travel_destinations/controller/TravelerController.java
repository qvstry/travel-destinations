package com.example.travel_destinations.controller;

import com.example.travel_destinations.entity.Traveler;
import com.example.travel_destinations.repository.TravelerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//REST Controller для Travelers
@RestController
@RequestMapping("/api/travelers")
@Tag(name = "Travelers", description = "API для управління Travelers")
@PreAuthorize("hasRole('ADMIN')") // доступно лише адмінам
public class TravelerController {

    @Autowired
    private TravelerRepository travelerRepository;

    //http://localhost:8080/api/travelers
    @GetMapping
    @Operation(summary = "Отримати всіх travelers")
    public List<Traveler> getAllTravelers() {
        return travelerRepository.findAll();
    }
    //count
    @GetMapping("/count")
    @Operation(summary = "Отримати кількість travelers")
    public ResponseEntity<Long> getTravelersCount() {
        long count = travelerRepository.count();
        return ResponseEntity.ok(count);
    }
    //traveler по ID
    @GetMapping("/{id}")
    @Operation(summary = "Отримати traveler по ID")
    public ResponseEntity<Traveler> getTravelerById(@PathVariable Long id) {
        Optional<Traveler> traveler = travelerRepository.findById(id);
        return traveler.map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    //по email
    @GetMapping("/email/{email}")
    @Operation(summary = "Отримати traveler по email")
    public ResponseEntity<Traveler> getTravelerByEmail(@PathVariable String email) {
        Optional<Traveler> traveler = travelerRepository.findByEmail(email);
        return traveler.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Пошук travelers на ім'я
    @GetMapping("/search")
    @Operation(summary = "Пошук travelers по імені")
    public List<Traveler> searchTravelersByName(@RequestParam String name) {
        return travelerRepository.findTravelerByName(name);
    }
    //по travel style
    @GetMapping("/travel-style/{style}")
    @Operation(summary = "Отримати travelers по стилю подорожей")
    public List<Traveler> getTravelersByStyle(@PathVariable Traveler.TravelStyle style) {
        return travelerRepository.findByTravelStyle(style);
    }

    //travelers які мають якесь destination як улюблене
    @GetMapping("/favorite-destination/{destinationId}")
    @Operation(summary = "Знайти travelers з улюбленим destination")
    public List<Traveler> getTravelersByFavoriteDestination(@PathVariable Long destinationId) {
        return travelerRepository.findByFavoriteDestination(destinationId);
    }

    // Створити нового traveler
    @PostMapping
    @Operation(summary = "Створити нового traveler")
    public ResponseEntity<Traveler> createTraveler(@Valid @RequestBody Traveler traveler) {
        // Перевірка email
        if (travelerRepository.existsByEmail(traveler.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("Error", "Email already exists")
                    .build();
        }
        Traveler savedTraveler = travelerRepository.save(traveler);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTraveler);
    }

    //Put update
    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити traveler")
    public ResponseEntity<Traveler> updateTraveler(@PathVariable Long id, @Valid @RequestBody Traveler travelerDetails) {
        return travelerRepository.findById(id)
                .map(traveler -> {
                    // Перевірка email
                    if (!traveler.getEmail().equals(travelerDetails.getEmail()) &&
                            travelerRepository.existsByEmail(travelerDetails.getEmail())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<Traveler>build();
                    }
                    // Оновлення всіх полів
                    traveler.setName(travelerDetails.getName());
                    traveler.setEmail(travelerDetails.getEmail());
                    traveler.setPhone(travelerDetails.getPhone());
                    traveler.setTravelStyle(travelerDetails.getTravelStyle());
                    traveler.setFavoriteDestinationId(travelerDetails.getFavoriteDestinationId());

                    return ResponseEntity.ok(travelerRepository.save(traveler));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Patch fav destination
    @PatchMapping("/{id}/favorite-destination")
    @Operation(summary = "Оновити fav destination")
    public ResponseEntity<Traveler> updateFavoriteDestination(@PathVariable Long id,@RequestParam Long destinationId) {
        return travelerRepository.findById(id)
                .map(traveler -> {
                    traveler.setFavoriteDestinationId(destinationId);
                    return ResponseEntity.ok(travelerRepository.save(traveler));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Patch travel style
    @PatchMapping("/{id}/travel-style")
    @Operation(summary = "Оновити travel style")
    public ResponseEntity<Traveler> updateTravelStyle(@PathVariable Long id, @RequestParam Traveler.TravelStyle travelStyle) {
        return travelerRepository.findById(id)
                .map(traveler -> {
                    traveler.setTravelStyle(travelStyle);
                    return ResponseEntity.ok(travelerRepository.save(traveler));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити traveler")
    public ResponseEntity<Void> deleteTraveler(@PathVariable Long id) {
        return travelerRepository.findById(id)
                .map(traveler -> {
                    travelerRepository.delete(traveler);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}