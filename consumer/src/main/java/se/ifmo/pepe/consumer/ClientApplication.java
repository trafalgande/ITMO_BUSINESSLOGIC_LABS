package se.ifmo.pepe.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@SpringBootApplication
@EnableJms
@Slf4j
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Component
    static class MessageConsumer implements MessageListener {
        @Override
        @JmsListener(destination = "blps_mq")
        public void onMessage(Message message) {
            log.info("Message consumed: {}", message);
        }
    }
}
