package se.ifmo.pepe.lab1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/notification")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                .enableStompBrokerRelay("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
