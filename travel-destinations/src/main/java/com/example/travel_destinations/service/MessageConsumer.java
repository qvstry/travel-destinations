package com.example.travel_destinations.service;

import com.example.travel_destinations.config.RabbitConfig;
import com.example.travel_destinations.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    // @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiveMessage(Message message) {
        System.out.println("Consumer: Отримано повідомлення від RabbitMQ.");
        System.out.println("   Message: " + message);

        switch (message.getAction()) {
            case "CREATE":
                System.out.println("Action: CREATE - Новий напрямок додано: " + message.getDescription());
                break;
            case "UPDATE":
                System.out.println("Action: UPDATE - Напрямок оновлено: " + message.getDescription());
                break;
            case "DELETE":
                System.out.println("Action: DELETE - Напрямок видалено: " + message.getDescription());
                break;
            case "ERROR":
                System.out.println("Action: ERROR - Обробка помилки");
                throw new RuntimeException("Тестова помилка");
            default:
                System.out.println("Unknown action: " + message.getAction());
        }

        System.out.println("Consumer: Повідомлення оброблено");
    }
}
