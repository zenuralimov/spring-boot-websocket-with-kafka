package com.websocket.springbootwebsocketwithkafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @Value("${kafka.topic}")
    private String kafkaTopic;

    private final SimpMessagingTemplate template;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public GreetingController(SimpMessagingTemplate template, KafkaTemplate<String, String> kafkaTemplate) {
        this.template = template;
        this.kafkaTemplate = kafkaTemplate;
    }

    @SubscribeMapping("/directly")
    public String greeting() {
        return "This message send directly back to the connected\n" +
                "user and does not pass through the message broker";
    }

    @MessageMapping("/mapped")
    public String greeting(String username) throws Exception {
        Thread.sleep(1000); // simulated delay
        return username + ", Hello! from mapped method (/app/mapped -> /topic/mapped)";
    }

    @MessageMapping("/sendTo")
    @SendTo("/topic/greetings")
    public String greetingSendTo(String username) throws Exception {
        Thread.sleep(3000);
        return username + ", Hello! This message sent to the specified destination " +
                "(/app/sendTo -> /topic/greetings)";
    }

    @MessageMapping("/simpTemplate")
    public void greetingWithSimpTemplate(String username) throws Exception {
        kafkaTemplate.send(kafkaTopic, username);
        Thread.sleep(5000);
        this.template.convertAndSend("/topic/greetings",
                username + ", Hello! This message sent to the specified destination " +
                        "using SimpMessagingTemplate (/app/simpTemplate -> /topic/greetings)");
    }

    @KafkaListener(topics = "kafka-topic", groupId = "groupId")
    public void greetingWithKafka(String username) throws Exception {
        Thread.sleep(7000);
        this.template.convertAndSend("/topic/greetings", username +
                ", Hello! This message sent through Topic Kafka to the specified destination " +
                "using SimpMessagingTemplate (/app/simpTemplate -> kafka-topic -> /topic/greetings)");
    }
}