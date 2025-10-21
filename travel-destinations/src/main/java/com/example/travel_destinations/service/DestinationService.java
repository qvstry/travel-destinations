package com.example.travel_destinations.service;

import com.example.travel_destinations.entity.Message;
import com.example.travel_destinations.repository.DestinationRepository;
import com.example.travel_destinations.entity.Destination;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository repository;
    private final MessageProducer messageProducer;

    // конструктор
    public DestinationService(DestinationRepository repository, MessageProducer messageProducer) {
        this.repository = repository;
        this.messageProducer = messageProducer;
    }

    // всі напрямки
    public List<Destination> getAllDestinations() {
        return repository.findAll();
    }

    // Get by ID
    public Destination getDestinationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не знайдено напрямок : " + id));
    }

    // новий напрямок
    public Destination createDestination(Destination destination) {
        System.out.println("Service: Створюємо новий напрямок: " + destination.getDescription());

        // зберігаємо
        Destination saved = repository.save(destination);

        // відправляємо повідомлення RabbitMQ
        Message message = new Message(
                saved.getDestinationId(),
                saved.getDescription(),
                saved.getCountry(),
                "CREATE"
        );
        messageProducer.sendMessage(message);

        return saved;
    }

    // 
    public Destination updateDestination(Long id, Destination details) {
        System.out.println("Service: Оновлюємо напрямок з id: " + id);

        Destination destination = getDestinationById(id);
        destination.setDescription(details.getDescription());
        destination.setCountry(details.getCountry());
        destination.setDescription(details.getDescription());

        Destination updated = repository.save(destination);

        // відправка повідомлення
        Message message = new Message(
                updated.getDestinationId(),
                updated.getDescription(),
                updated.getCountry(),
                "UPDATE"
        );
        messageProducer.sendMessage(message);

        return updated;
    }

    // вилалення
    public void deleteDestination(Long id) {
        System.out.println("Service: Видаляємо напрямок з id: " + id);

        Destination destination = getDestinationById(id);

        // відправка до видалення
        Message message = new Message(
                destination.getDestinationId(),
                destination.getDescription(),
                destination.getCountry(),
                "DELETE"
        );
        messageProducer.sendMessage(message);

        repository.deleteById(id);
    }
}