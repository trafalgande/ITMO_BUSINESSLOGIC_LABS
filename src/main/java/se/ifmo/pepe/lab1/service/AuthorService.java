package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Author;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.AuthorRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

import java.util.ArrayList;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, NotificationRepository notificationRepository) {
        this.authorRepository = authorRepository;
        this.notificationRepository = notificationRepository;
    }

    public Author fetchAuthorById(Long id) {
        return authorRepository.findById(id).isPresent() ? authorRepository.findById(id).get() : null;
    }

    public ArrayList<CustomNotification> fetchAllNewNotificationsByAuthorId(Long authorId) {
        ArrayList<CustomNotification> notifications = new ArrayList<>(notificationRepository.findAllByAuthorAndIsNew(fetchAuthorById(authorId), true));
        for (CustomNotification n : notifications) {
            n.setIsNew(false);
            notificationRepository.save(n);
        }
        return notifications;
    }
}
