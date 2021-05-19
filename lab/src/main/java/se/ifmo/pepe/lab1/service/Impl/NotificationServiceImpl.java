package se.ifmo.pepe.lab1.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;
import se.ifmo.pepe.lab1.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @Autowired
    public NotificationServiceImpl(SimpMessagingTemplate simpMessagingTemplate, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createNotification(User user, String message) {
        notificationRepository.save(new CustomNotification()
                .setUser(user)
                .setMessage(message)
                .setIsNew(true));
    }

    @Override
    public void sendNotification(Long userId, String message) {

        if (userRepository.findById(userId).isPresent()) {
            createNotification(userRepository.findById(userId).get(), message);
            simpMessagingTemplate.convertAndSend("/topic/notification", message);
        }
    }

    @Override
    public void sendNotification(String username, String message) {
        simpMessagingTemplate.convertAndSend("/topic/notification." + username, message);
    }
}
