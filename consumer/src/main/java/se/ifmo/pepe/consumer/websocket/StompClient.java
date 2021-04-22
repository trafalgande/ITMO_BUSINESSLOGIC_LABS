package se.ifmo.pepe.consumer.websocket;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class StompClient {
    private static String URL = "ws://localhost:18787/notification";

    public static void main(String[] args) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("login", "lmao");
        stompHeaders.add("passcode", "lmao");
        stompHeaders.add("Authorization", "Basic bG1hbzpsbWFv");
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new CustomStompSessionHandler();
        stompClient.connect(URL, new WebSocketHttpHeaders(), stompHeaders, sessionHandler);

        new Scanner(System.in).nextLine();
    }
}
