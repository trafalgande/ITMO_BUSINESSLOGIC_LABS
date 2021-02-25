package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public UserService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public User fetchUserById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get() : null;
    }

    public ArrayList<CustomNotification> fetchAllNewNotificationsByUserId(Long userId) {
        ArrayList<CustomNotification> notifications = new ArrayList<>(notificationRepository.findAllByUserAndIsNew(fetchUserById(userId), true));
        for (CustomNotification n : notifications) {
            n.setIsNew(false);
            notificationRepository.save(n);
        }
        return notifications;
    }
}
