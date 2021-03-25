package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    private void createNotification(User user, String message) {
        notificationRepository.save(new CustomNotification()
                .setUser(user)
                .setMessage(message)
                .setIsNew(true));
    }

    public void sendNotification(Long userId, String message) {
        if (userRepository.findById(userId).isPresent())
        createNotification(userRepository.findById(userId).get() , message);
    }

}
