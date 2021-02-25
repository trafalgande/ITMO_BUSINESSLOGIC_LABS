package se.ifmo.pepe.lab1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.pepe.lab1.model.CustomNotification;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.AuthorService;
import se.ifmo.pepe.lab1.service.SkinService;

import java.util.ArrayList;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final SkinService skinService;
    private final AuthorService authorService;

    @Autowired
    public ApiController(SkinService skinService, AuthorService authorService) {
        this.skinService = skinService;
        this.authorService = authorService;
    }

    @RequestMapping("/{id}/notifications")
    public ArrayList<CustomNotification> findAllNewNotifications(@PathVariable(name = "id") Long author_id) {
        return authorService.fetchAllNewNotificationsByAuthorId(author_id);
    }

    @RequestMapping("/skins/add")
    public Skin addSkin(@RequestParam(name = "title") String title,
                        @RequestParam(name = "description") String description,
                        @RequestParam(name = "price", required = false) Double price,
                        @RequestParam(name = "sex", required = false) String sex,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "author_id") Long authorId) {
        return skinService.saveSkin(new Skin()
                .setTitle(title)
                .setDescription(description)
                .setPrice(price != null ? price : 0)
                .setSex(sex)
                .setTag(tag)
                .setAuthor(authorService.fetchAuthorById(authorId))
        );
    }

    @RequestMapping("/skins/find/{by}")
    public ArrayList<Skin> findAllSkinsBy(@PathVariable(name = "by") String by,
                                          @RequestParam(name = "value", required = false) String value) {

        switch (by) {
            case "tag":
                return skinService.fetchAllSkinsByTag(value);
            case "sex":
                return skinService.fetchAllSkinsBySex(value);
            case "all":
                return skinService.fetchAllSkins();
        }
        return null;
    }

    @RequestMapping("/skins/buy/{skin_id}")
    public void buySkin(@PathVariable(name = "skin_id") Long skinId) {
        //TODO
    }
}
