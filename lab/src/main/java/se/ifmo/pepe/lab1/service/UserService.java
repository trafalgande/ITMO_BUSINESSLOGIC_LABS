package se.ifmo.pepe.lab1.service;

import se.ifmo.pepe.lab1.exception.SkinException;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User fetchUserById(Long id);

    User fetchUserByName(String name);

    List<CustomNotification> fetchAllNewNotificationsByUserId(Long userId);

    boolean saveUser(User user);

    boolean deleteUser(Long userId);

    Skin purchaseSkin(Long userId, Long skinId) throws SkinException;

    List<User> fetchAllAuthors();
}
