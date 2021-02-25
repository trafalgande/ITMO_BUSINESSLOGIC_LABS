package se.ifmo.pepe.lab1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/skins/find/all")
    public ArrayList<Skin> findAllSkins() {
        return skinService.fetchAllSkins();
    }

   /* @RequestMapping("/skins/find/{parameter}")
    public void findAllSkinsByParameter(@PathVariable(name = "parameter")String parameter) {

        switch (parameter) {
            case "female" -> {
               return skinService.fetchAllSkinsBy(parameter)
            }
        }
    }*/

    @RequestMapping("/skins/add")
    public Skin addSkin(@RequestParam(name = "title") String title,
                        @RequestParam(name = "description") String description,
                        @RequestParam(name = "price", required = false) Double price,
                        @RequestParam(name = "author_id") Long authorId) {
        return skinService.saveSkin(new Skin()
                .setTitle(title)
                .setDescription(description)
                .setPrice(price != null ? price : 0)
                .setAuthor(authorService.fetchAuthorById(authorId))
        );
    }
}
