package se.ifmo.pepe.lab1.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.SkinService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/sudo")
public class AdminController {

    private final SkinService skinService;

    @Autowired
    public AdminController(SkinService skinService) {
        this.skinService = skinService;
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

    @RequestMapping("/approve/{id}")
    public Skin approveSkinById(@RequestParam(name = "u") String username,
                                @RequestParam(name = "p") String password,
                                @PathVariable(name = "id") Long skinId) {
        
    }
}
