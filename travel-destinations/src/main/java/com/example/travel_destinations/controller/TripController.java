package com.example.travel_destinations.controller;

import com.example.travel_destinations.entity.Trip;
import com.example.travel_destinations.entity.Destination;
import com.example.travel_destinations.repository.TripRepository;
import com.example.travel_destinations.repository.TravelerRepository;
import com.example.travel_destinations.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//REST Controller для Trips
@RestController
@RequestMapping("/api/trips")
@Tag(name = "Trips", description = "API для управління подорожами")
public class TripController {

    // залежності
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    //всі trips
    @GetMapping("/public/all")
    @Operation(summary = "Отримати всі подорожі")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }
    //count

    //trip по id
    @GetMapping("public/{id}")
    @Operation(summary = "Отримати подорож по ID")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Optional<Trip> trip = tripRepository.findById(id);
        return trip.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/manager/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Отримати кількість подорожей")
    public ResponseEntity<Long> getTripsCount() {
        long count = tripRepository.count();
        return ResponseEntity.ok(count);
    }
    //trips by traveler
    @GetMapping("/manager/traveler/{travelerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Отримати подорожі traveler")
    public List<Trip> getTripsByTraveler(@PathVariable Long travelerId) {
        return tripRepository.findByTravelerTravelerId(travelerId);
    }
    // trips з бюджетом <=
    @GetMapping("/budget/{maxBudget}")
    @Operation(summary = "Отримати подорожі в межах бюджету")
    public List<Trip> getTripsByMaxBudget(@PathVariable BigDecimal maxBudget) {
        return tripRepository.findByBudgetLessThanEqual(maxBudget);
    }

    //date range
    @GetMapping("/dates")
    @Operation(summary = "Отримати подорожі по датах")
    public List<Trip> getTripsByDateRange(@RequestParam String startDate,@RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return tripRepository.findByStartDateBetween(start, end);
    }

    //trips по destination
    @GetMapping("/destination/{destinationId}")
    @Operation(summary = "Отримати подорожі до певного місця")
    public List<Trip> getTripsToDestination(@PathVariable Long destinationId) {
        return tripRepository.findTripsToDestination(destinationId);
    }

    // POST нову trip
    @PostMapping
    @Operation(summary = "Створити нову подорож")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip, @RequestParam Long travelerId) {
        return travelerRepository.findById(travelerId)
                .map(traveler -> {
                    // валідація дат
                    if (trip.getStartDate().isAfter(trip.getEndDate())) {
                        return ResponseEntity.badRequest().<Trip>build();
                    }
                    trip.setTraveler(traveler);
                    if (trip.getStatus() == null) {
                        trip.setStatus(Trip.TripStatus.PLANNED);// по дефолтуу
                    }
                    Trip savedTrip = tripRepository.save(trip);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);
                })
                .orElse(ResponseEntity.badRequest().build()); // якщо traveler не знайдено
    }

    //Put
    @PutMapping("/manager/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Повністю оновити подорож")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @Valid @RequestBody Trip tripDetails) {
        return tripRepository.findById(id)
                .map(trip -> {
                    // Валідація дат
                    if (tripDetails.getStartDate().isAfter(tripDetails.getEndDate())) {
                        return ResponseEntity.badRequest().<Trip>build();
                    }
                    trip.setName(tripDetails.getName());
                    trip.setDescription(tripDetails.getDescription());
                    trip.setStartDate(tripDetails.getStartDate());
                    trip.setEndDate(tripDetails.getEndDate());
                    trip.setBudget(tripDetails.getBudget());
                    trip.setStatus(tripDetails.getStatus());

                    return ResponseEntity.ok(tripRepository.save(trip));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //patch status
    @PatchMapping("/{id}/status")
    @Operation(summary = "Оновити статус подорожі")
    public ResponseEntity<Trip> updateTripStatus(@PathVariable Long id,@RequestParam Trip.TripStatus status) {
        return tripRepository.findById(id)
                .map(trip -> {
                    trip.setStatus(status);
                    return ResponseEntity.ok(tripRepository.save(trip));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //patch budget
    @PatchMapping("/{id}/budget")
    @Operation(summary = "Оновити бюджет подорожі")
    public ResponseEntity<Trip> updateTripBudget(@PathVariable Long id, @RequestParam BigDecimal budget) {
        if (budget.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return tripRepository.findById(id)
                .map(trip -> {
                    trip.setBudget(budget);
                    return ResponseEntity.ok(tripRepository.save(trip));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //patch dates
    @PatchMapping("/{id}/dates")
    @Operation(summary = "Оновити дати подорожі")
    public ResponseEntity<Trip> updateTripDates(@PathVariable Long id,@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            return ResponseEntity.badRequest().build();
        }
        return tripRepository.findById(id)
                .map(trip -> {
                    trip.setStartDate(start);
                    trip.setEndDate(end);
                    return ResponseEntity.ok(tripRepository.save(trip));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Post
    @PostMapping("/{tripId}/destinations/{destinationId}")
    @Operation(summary = "Додати місце до подорожі")
    public ResponseEntity<Trip> addDestinationToTrip(@PathVariable Long tripId, @PathVariable Long destinationId) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        Optional<Destination> destOpt = destinationRepository.findById(destinationId);

        if (tripOpt.isPresent() && destOpt.isPresent()) {
            Trip trip = tripOpt.get();
            Destination destination = destOpt.get();

            // Перевірка чи destination вже доданий
            if (!trip.getDestinations().contains(destination)) {
                trip.getDestinations().add(destination);
                Trip savedTrip = tripRepository.save(trip);
                return ResponseEntity.ok(savedTrip);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Вже існує
            }
        }

        return ResponseEntity.notFound().build();
    }

    //delete destin from trip
    @DeleteMapping("/admin/{tripId}/destinations/{destinationId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити місце з подорожі")
    public ResponseEntity<Trip> removeDestinationFromTrip(@PathVariable Long tripId, @PathVariable Long destinationId) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        Optional<Destination> destOpt = destinationRepository.findById(destinationId);

        if (tripOpt.isPresent() && destOpt.isPresent()) {
            Trip trip = tripOpt.get();
            Destination destination = destOpt.get();

            if (trip.getDestinations().contains(destination)) {
                trip.getDestinations().remove(destination);
                Trip savedTrip = tripRepository.save(trip);
                return ResponseEntity.ok(savedTrip);
            } else {
                return ResponseEntity.notFound().build(); // Зв'язок не існує
            }
        }
        return ResponseEntity.notFound().build();
    }


    //delete trip
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити подорож")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        return tripRepository.findById(id)
                .map(trip -> {
                    tripRepository.delete(trip);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}