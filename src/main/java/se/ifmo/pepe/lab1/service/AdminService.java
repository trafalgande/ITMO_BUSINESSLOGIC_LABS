package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Role;
import se.ifmo.pepe.lab1.model.Skin;

@Service
public class AdminService {

    private final SkinService skinService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public AdminService(SkinService skinService, UserService userService, NotificationService notificationService) {
        this.skinService = skinService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public void decline(Long skinId) {
        Skin skin = skinService.fetchSkinById(skinId);
        skinService.removeSkinById(skin.getId());
        notificationService.sendNotification(skin.getUser().getId(),
                String.format("Your skin#%d was declined! :(", skin.getId()));
    }

    public void approve(Long skinId) {
        Skin skin = skinService.saveSkin(skinService.fetchSkinById(skinId).setApproved(true));
        notificationService.sendNotification(skin.getUser().getId(),
                String.format("Your skin#%d was approved! :)", skin.getId()));
    }

}
