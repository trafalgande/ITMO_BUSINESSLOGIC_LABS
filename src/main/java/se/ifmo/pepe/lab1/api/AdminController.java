package se.ifmo.pepe.lab1.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.AuthorService;
import se.ifmo.pepe.lab1.service.NotificationService;
import se.ifmo.pepe.lab1.service.SkinService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/sudo")
public class AdminController {

    private final SkinService skinService;
    private final AuthorService authorService;
    private final NotificationService notificationService;

    @Autowired
    public AdminController(SkinService skinService, AuthorService authorService, NotificationService notificationService) {
        this.skinService = skinService;
        this.authorService = authorService;
        this.notificationService = notificationService;
    }

    @RequestMapping("/skins")
    public ArrayList<Skin> findAllSkins(@RequestParam(name = "u") String username,
                                        @RequestParam(name = "p") String password,
                                        @RequestParam(name = "approved", required = false) Optional<Boolean> areApproved) {
        if (username.equals("admin") && password.equals("admin"))
            if (areApproved.isPresent())
                return skinService.fetchAllApprovedSkins(areApproved.get());
            else
                return skinService.fetchAllSkins();
        return null;
    }

    @RequestMapping("/skin/{id}")
    public Skin findSkinById(@RequestParam(name = "u") String username,
                             @RequestParam(name = "p") String password,
                             @PathVariable(name = "id") Long skinId) {
        if (username.equals("admin") && password.equals("admin"))
            return skinService.fetchSkinById(skinId);
        return null;
    }

    @RequestMapping("/{action}/{id}")
    public Skin resolveSkinById(@RequestParam(name = "u") String username,
                                @RequestParam(name = "p") String password,
                                @PathVariable(name = "action") String action,
                                @PathVariable(name = "id") Long skinId) {
        if (username.equals("admin") && password.equals("admin"))
            switch (action) {
                case "approve" : {
                    Skin skin = skinService.saveSkin(skinService.fetchSkinById(skinId).setApproved(true));
                    notificationService.createNotification(authorService.fetchAuthorById(skin.getAuthor().getId()),
                            String.format("Your skin#%d was approved! :)", skin.getId()));
                    return skin;
                }
                case "decline" : {
                    Skin skin =  skinService.saveSkin(skinService.fetchSkinById(skinId).setApproved(false));
                    notificationService.createNotification(authorService.fetchAuthorById(skin.getAuthor().getId()),
                            String.format("Your skin#%d was declined! :(", skin.getId()));
                    return skin;
                }
            }
        return null;
    }
}
