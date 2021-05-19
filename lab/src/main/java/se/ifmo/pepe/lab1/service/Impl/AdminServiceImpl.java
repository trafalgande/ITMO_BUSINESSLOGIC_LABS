package se.ifmo.pepe.lab1.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.pepe.lab1.exception.SkinException;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.AdminService;
import se.ifmo.pepe.lab1.service.NotificationService;
import se.ifmo.pepe.lab1.service.SkinService;

import javax.persistence.Transient;
import javax.transaction.Transactional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final SkinService skinService;
    private final NotificationService notificationService;
    private final TransactionTemplate template;

    @Autowired
    public AdminServiceImpl(SkinService skinService,
                            NotificationService notificationService,
                            PlatformTransactionManager txManager) {
        this.skinService = skinService;
        this.notificationService = notificationService;
        this.template = new TransactionTemplate(txManager);
    }

    @Override
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

    @Override
    public Boolean approve(Long skinId) throws SkinException {
        Skin skinToApprove = skinService.fetchSkinById(skinId);
        if (skinToApprove == null)
            throw new SkinException("There's no such skin, mr.Admin");

        template.execute(transactionStatus -> {
            try {
                skinToApprove.setApproved(true);
                Skin skin = skinService.saveSkin(skinToApprove);
                log.info("Sending notification to {}", skin.getUser());
                notificationService.sendNotification(skin.getUser().getUsername(),
                        String.format("New skin \"%s\" from %s", skin.getTitle(), skin.getUser().getUsername()));
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
