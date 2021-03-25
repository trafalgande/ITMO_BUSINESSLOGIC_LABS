package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Role;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.pepe.lab1.exception.PaymentException;
import se.ifmo.pepe.lab1.exception.SkinException;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.repository.SkinRepository;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.repository.RoleRepository;
import se.ifmo.pepe.lab1.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SkinRepository skinRepository;
    private final PlatformTransactionManager txManager;
    private final TransactionTemplate template;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(RoleRepository roleRepository,
                       UserRepository userRepository,
                       NotificationRepository notificationRepository,
                       SkinRepository skinRepository,
                       PlatformTransactionManager txManager) {
        this.roleRepository = roleRepository;
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

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername()).isPresent() ? userRepository.findByUsername(user.getUsername()).get() : null;

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(roleRepository.findByName("ROLE_USER").isPresent() ? Collections.singleton(roleRepository.findByName("ROLE_USER").get()) : null);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByUsername(username).isPresent())
            return userRepository.findByUsername(username).get();
        else
            throw new UsernameNotFoundException("User not found");
    }

    public Skin purchaseSkin(Long userId, Long skinId) throws SkinException {
        User buyer = userRepository.findById(userId).get();
        Skin desired = skinRepository.findById(skinId).isPresent() ? skinRepository.findById(skinId).get() : null;

        if (desired == null)
            throw new SkinException("There's no such skin");

        if (!desired.getApproved())
            throw new SkinException("Skin is not approved yet");

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
        return desired;
    }

}
