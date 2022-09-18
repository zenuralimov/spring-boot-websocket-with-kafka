package com.websocket.springbootwebsocketwithkafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class SpringBootWebsocketWithKafkaApplication {

    @Value("${kafka.topic}")
    private String kafkaTopic;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebsocketWithKafkaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> kafkaTemplate.send(kafkaTopic, "Topic Kafka");
    }
}
