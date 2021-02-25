package se.ifmo.pepe.lab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<CustomNotification, Long>, CrudRepository<CustomNotification, Long> {
    List<CustomNotification> findAllByUserAndIsNew(User user, Boolean areNew);
}
