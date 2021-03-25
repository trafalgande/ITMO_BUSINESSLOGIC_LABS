package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.pepe.lab1.exception.PaymentException;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.SkinRepository;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SkinRepository skinRepository;
    private final PlatformTransactionManager txManager;
    private final TransactionTemplate template;

    @Autowired
    public UserService(UserRepository userRepository, NotificationRepository notificationRepository, SkinRepository skinRepository, TransactionTemplate tt, PlatformTransactionManager txManager, TransactionTemplate template) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.skinRepository = skinRepository;

        this.txManager = txManager;
        this.template = new TransactionTemplate(this.txManager);
    }

    public User fetchUserById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get() : userRepository.findById(1L).get(); // if no user present -> belongs to super-user aka admin (id: 1)
    }

    public ArrayList<CustomNotification> fetchAllNewNotificationsByUserId(Long userId) {
        ArrayList<CustomNotification> notifications = new ArrayList<>(notificationRepository.findAllByUserAndIsNew(fetchUserById(userId), true));
        for (CustomNotification n : notifications) {
            n.setIsNew(false);
            notificationRepository.save(n);
        }
        return notifications;
    }

    public Skin purchaseSkin(Long userId, Long skinId) {
        User buyer = userRepository.findById(userId).isPresent() ? userRepository.findById(userId).get() : null;
        Skin desired = skinRepository.findById(skinId).isPresent() ? skinRepository.findById(skinId).get() : null;
        assert desired != null;
        assert buyer != null;

        if (!desired.getApproved())
            return null;

        template.execute(transactionStatus -> {
            try {
                buyer.getWallet().withdraw(desired.getPrice(), desired.getUser().getWallet());
                userRepository.save(buyer);
                userRepository.save(desired.getUser());
                return desired;
            } catch (PaymentException e) {
                transactionStatus.setRollbackOnly();
                e.printStackTrace();
            }
            return desired;
        });
        return null;
    }

}
