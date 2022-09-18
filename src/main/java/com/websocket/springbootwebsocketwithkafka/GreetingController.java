package com.websocket.springbootwebsocketwithkafka;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

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
}