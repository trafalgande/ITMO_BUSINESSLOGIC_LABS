package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Author;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Author author, String message) {
        notificationRepository.save(new CustomNotification()
                .setAuthor(author)
                .setMessage(message)
                .setIsNew(true));
    }

}
