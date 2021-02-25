package se.ifmo.pepe.lab1.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.NotificationService;
import se.ifmo.pepe.lab1.service.SkinService;
import se.ifmo.pepe.lab1.service.UserService;

import java.util.ArrayList;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final SkinService skinService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public ApiController(SkinService skinService, UserService userService, NotificationService notificationService) {
        this.skinService = skinService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "${ApiController.findAllNewNotifications}")
    @GetMapping("/{id}/notifications")
    public ArrayList<CustomNotification> findAllNewNotifications(
            @ApiParam("id") @PathVariable(name = "id") Long userId) {
        return userService.fetchAllNewNotificationsByUserId(userId);
    }


    @ApiOperation(value = "${ApiController.addSkin}")
    @PostMapping("/skins/add")
    public Skin addSkin(@ApiParam("title") @RequestParam(name = "title") String title,
                        @ApiParam("description") @RequestParam(name = "description") String description,
                        @ApiParam("price") @RequestParam(name = "price", required = false) Double price,
                        @ApiParam("sex") @RequestParam(name = "sex", required = false) String sex,
                        @ApiParam("tag") @RequestParam(name = "tag", required = false) String tag,
                        @ApiParam("user_id") @RequestParam(name = "user_id") Long userId) {
        return skinService.saveSkin(new Skin()
                .setTitle(title)
                .setDescription(description)
                .setPrice(price != null ? price : 0)
                .setSex(sex)
                .setTag(tag)
                .setUser(userService.fetchUserById(userId))
        );
    }


    @ApiOperation(value = "${ApiController.findAllSkinsBy}")
    @GetMapping("/skins/find/{by}")
    public ArrayList<Skin> findAllSkinsBy(@ApiParam("by") @PathVariable(name = "by") String by,
                                          @ApiParam("value") @RequestParam(name = "value", required = false) String value) {
        switch (by) {
            case "tag":
                return skinService.fetchAllSkinsByTag(value);
            case "sex":
                return skinService.fetchAllSkinsBySex(value);
            case "all":
                return skinService.fetchAllApprovedSkins(true); // show users only approved skins
        }
        return null;
    }

    @ApiOperation(value = "${ApiController.buySkin}")
    @GetMapping("/skins/buy/{skin_id}")
    public HttpStatus buySkin(@ApiParam("skin_id") @PathVariable(name = "skin_id") Long skinId,
                              @ApiParam("user_id") @RequestParam(name = "user_id") Long userId,
                              @ApiParam("available") @RequestParam(name = "available") Double available) {
        Skin desired = skinService.fetchSkinById(skinId);
        if (available >= desired.getPrice()) {
            notificationService.sendNotification(userId, String.format("You've successfully bought skin#%d", skinId));
            return HttpStatus.OK;
        } else {
            notificationService.sendNotification(userId, String.format("You haven't bought skin#%d", skinId));
            return HttpStatus.BAD_REQUEST;
        }

        /*
         * TODO: YOU SHOULD BE ABLE TO LOGIN AS USER, THEREFORE HAVING MONEY AND BEING ABLE TO BUY SKINS
         *  THUS, WITHDRAWING SHOULD BE IMPLEMENTED AS SOON AS REGISTRATION IMPLEMENTED
         * */
    }
}
