package se.ifmo.pepe.lab1.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;

@RestController
@RequestMapping("standalone/publish")
@Slf4j
public class MessageProducer {

    private final Queue queue;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageProducer(Queue queue, JmsTemplate jmsTemplate) {
        this.queue = queue;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("{message}")
    public void publish(@PathVariable("message") String message) {
        jmsTemplate.convertAndSend("blps_mq", message);
        log.info("Message published: {}", message);
    }
}
