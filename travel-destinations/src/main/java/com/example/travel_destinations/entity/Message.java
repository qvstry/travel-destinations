package com.example.travel_destinations.entity;

import java.time.LocalDateTime;

public class Message {

    private Long destinationId;
    private String description;
    private String country;
    private String action; // CREATE, UPDATE, DELETE
    private LocalDateTime timestamp;

    // без параметрів
    public Message() {
    }

    // конструктор з параметрами
    public Message(Long destinationId, String description, String country, String action) {
        this.destinationId = destinationId;
        this.description = description;
        this.country = country;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TravelMessage{" +
                "destinationId=" + destinationId +
                ", description='" + description + '\'' +
                ", country='" + country + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}