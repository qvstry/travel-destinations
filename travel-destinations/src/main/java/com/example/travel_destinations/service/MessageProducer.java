package com.example.travel_destinations.service;

import com.example.travel_destinations.config.RabbitConfig;
import com.example.travel_destinations.entity.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Message message) {
        System.out.println("\n Producer: Відправляємо повідомлення до RabbitMQ");
        System.out.println("   Повідомлення: " + message);

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                message
        );

        System.out.println(" Producer: Повідомлення відправлено!.\n");
    }
}