package se.ifmo.pepe.lab1.service;

import org.springframework.http.ResponseEntity;
import se.ifmo.pepe.lab1.model.User;

public interface NotificationService {
    void createNotification(User user, String message);

    void sendNotification(Long userId, String message);

    void sendNotification(String username, String message);

}
