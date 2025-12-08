package com.example.shop.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("orders", 1, (short) 1);
    }

    @Bean
    public NewTopic productTopic() {
        return new NewTopic("products", 1, (short) 1);
    }
}