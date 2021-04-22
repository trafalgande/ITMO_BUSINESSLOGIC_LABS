package se.ifmo.pepe.lab1.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBroadcastController {
 

    @MessageMapping("/notification")
    @SendTo("/topic/notification")
    public String send(String message)  {
        return message;
    }
}