package se.ifmo.pepe.lab1.controller;


import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.ifmo.pepe.lab1.exception.SkinException;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.AdminService;
import se.ifmo.pepe.lab1.service.SkinService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final SkinService skinService;
    private final AdminService adminService;

    @Autowired
    public AdminController(SkinService skinService, AdminService adminService) {
        this.skinService = skinService;
        this.adminService = adminService;
    }

    @GetMapping("/skins")
    public ArrayList<Skin> findAllSkins(@ApiParam("approved") @RequestParam(name = "approved", required = false) Optional<Boolean> areApproved) {
        if (areApproved.isPresent())
            return skinService.fetchAllApprovedSkins(areApproved.get());
        else
            return skinService.fetchAllSkins();
    }

    @GetMapping(value = "/skin/{id}")
    public Skin findSkinById(@ApiParam("id") @PathVariable(name = "id") Long skinId) {
        return skinService.fetchSkinById(skinId);
    }


    @GetMapping("/approve/{id}")
    public HttpStatus approveSkinById(@ApiParam("id") @PathVariable(name = "id") Long skinId) throws SkinException {
        if (!adminService.approve(skinId))
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }

    @GetMapping("/decline/{id}")
    public HttpStatus declineSkinById(@ApiParam("id") @PathVariable(name = "id") Long skinId) throws SkinException {
        if (!adminService.decline(skinId))
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;

    }

    /***
     * Use approveSkinById() or declineSkinById() instead
     * */
    @Deprecated
    @GetMapping("/{action}/{id}")
    public HttpStatus resolveSkinById(@ApiParam("username") @RequestParam(name = "u") String username,
                                      @ApiParam("password") @RequestParam(name = "p") String password,
                                      @ApiParam("action") @PathVariable(name = "action") String action,
                                      @ApiParam("id") @PathVariable(name = "id") Long skinId) throws SkinException {
        if (username.equals("admin") && password.equals("admin"))
            switch (action) {
                case "approve": {
                    adminService.approve(skinId);
                    return HttpStatus.OK;
                }
                case "decline": {
                    adminService.decline(skinId);
                    return HttpStatus.OK;
                }
            }
        return HttpStatus.NOT_FOUND;
    }
}
