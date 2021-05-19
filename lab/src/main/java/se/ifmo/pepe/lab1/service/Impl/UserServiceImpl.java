package se.ifmo.pepe.lab1.service.Impl;

import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
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
import se.ifmo.pepe.lab1.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SkinRepository skinRepository;
    private final TransactionTemplate template;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository,
                           UserRepository userRepository,
                           NotificationRepository notificationRepository,
                           SkinRepository skinRepository,
                           PlatformTransactionManager txManager,
                           @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.skinRepository = skinRepository;
        this.template = new TransactionTemplate(txManager);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User fetchUserById(Long id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id).get() : userRepository.findById(1L).get(); // if no user present -> belongs to super-user aka admin (id: 1)
    }

    @Override
    public User fetchUserByName(String name) {
        return userRepository.findByUsername(name).isPresent() ? userRepository.findByUsername(name).get() : userRepository.findById(1L).get(); // if no user present -> belongs to super-user aka admin (id: 1)
    }

    @Override
    public ArrayList<CustomNotification> fetchAllNewNotificationsByUserId(Long userId) {
        ArrayList<CustomNotification> notifications = new ArrayList<>(notificationRepository.findAllByUserAndIsNew(fetchUserById(userId), true));
        for (CustomNotification n : notifications) {
            n.setIsNew(false);
            notificationRepository.save(n);
        }
        return notifications;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<User> fetchAllAuthors() {
        return Observable.fromIterable(userRepository.findAllBySkinCounterGreaterThan(0L))
                .distinct(User::getUsername).toList().blockingGet();
    }

}
