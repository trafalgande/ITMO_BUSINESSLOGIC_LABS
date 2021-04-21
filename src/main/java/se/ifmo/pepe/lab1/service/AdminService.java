package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.pepe.lab1.exception.SkinException;
import se.ifmo.pepe.lab1.model.Skin;

@Service
public class AdminService {

    private final SkinService skinService;
    private final NotificationService notificationService;
    private final TransactionTemplate template;

    @Autowired
    public AdminService(SkinService skinService,
                        NotificationService notificationService,
                        PlatformTransactionManager txManager) {
        this.skinService = skinService;
        this.notificationService = notificationService;
        this.template = new TransactionTemplate(txManager);
    }

    public Boolean decline(Long skinId) throws SkinException {
        Skin skinToDecline = skinService.fetchSkinById(skinId);
        if (skinToDecline == null)
            throw new SkinException("There's no such skin, mr.Admin");

        template.execute(transactionStatus -> {
            try {
                skinToDecline.setApproved(false);
                Skin skin = skinService.saveSkin(skinToDecline);
                notificationService.sendNotification(skin.getUser().getId(),
                        String.format("Your skin#%d was declined! :(", skin.getId()));
                return true;
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                notificationService.sendNotification(skinToDecline.getUser().getId(),
                        "Something went wrong. Transaction has rolled back");
                e.printStackTrace();
            }
            return false;
        });
        return false;
    }

    public Boolean approve(Long skinId) throws SkinException {
        Skin skinToApprove = skinService.fetchSkinById(skinId);
        if (skinToApprove == null)
            throw new SkinException("There's no such skin, mr.Admin");

        template.execute(transactionStatus -> {
            try {
                skinToApprove.setApproved(true);
                Skin skin = skinService.saveSkin(skinToApprove);
                notificationService.sendNotification(skin.getUser().getId(),
                        String.format("Your skin#%d was approved! :)", skin.getId()));
                return true;
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                notificationService.sendNotification(skinToApprove.getUser().getId(),
                        "Something went wrong. Transaction has rolled back");
                e.printStackTrace();
            }
            return false;
        });
        return false;
    }

}
