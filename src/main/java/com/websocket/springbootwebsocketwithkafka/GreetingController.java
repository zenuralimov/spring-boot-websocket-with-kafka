package com.websocket.springbootwebsocketwithkafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    private final SimpMessagingTemplate template;

    public GreetingController(SimpMessagingTemplate template) {
        this.template = template;
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
        Thread.sleep(2000);
        return username + ", Hello! This message sent to the specified destination " +
                "(/app/sendTo -> /topic/greetings)";
    }

    @MessageMapping("/simpTemplate")
    public void greetingWithSimpTemplate(String username) throws Exception {
        Thread.sleep(3000);
        this.template.convertAndSend("/topic/greetings",
                username + ", Hello! This message sent to the specified destination " +
                        "using SimpMessagingTemplate (/app/simpTemplate -> /topic/greetings)");
    }

    @KafkaListener(topics="kafka-topic", groupId = "groupId")
    public void greetingWithKafka(String message) throws Exception {
        Thread.sleep(15000);
        this.template.convertAndSend("/topic/greetings", "Hello from " + message);
    }
}