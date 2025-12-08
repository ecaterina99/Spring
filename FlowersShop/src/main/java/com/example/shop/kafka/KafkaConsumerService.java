package com.example.shop.kafka;

import com.example.shop.controllers.EventsController;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EventsController eventsController;

    public KafkaConsumerService(EventsController eventsController) {
        this.eventsController = eventsController;
    }

    @KafkaListener(topics = "orders", groupId = "flower-shop-group")
    public void listenOrders(String message) {
        System.out.println("Order event received: " + message);
        eventsController.addEvent("ORDER: " + message);
    }

    @KafkaListener(topics = "products", groupId = "flower-shop-group")
    public void listenProducts(String message) {
        System.out.println("Product event received: " + message);
        eventsController.addEvent("PRODUCT: " + message);
    }
}