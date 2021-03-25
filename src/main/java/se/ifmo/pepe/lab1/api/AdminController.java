package se.ifmo.pepe.lab1.api;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.service.AdminService;
import se.ifmo.pepe.lab1.service.NotificationService;
import se.ifmo.pepe.lab1.service.SkinService;
import se.ifmo.pepe.lab1.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/sudo")
public class AdminController {

    private final SkinService skinService;
    private final AdminService adminService;

    @Autowired
    public AdminController(SkinService skinService, AdminService adminService) {
        this.skinService = skinService;
        this.adminService = adminService;
    }


    @ApiOperation(value = "${AdminController.findAllSkins}")
    @GetMapping("/skins")
    public ArrayList<Skin> findAllSkins(@ApiParam("username") @RequestParam(name = "u") String username,
                                        @ApiParam("password") @RequestParam(name = "p") String password,
                                        @ApiParam("approved") @RequestParam(name = "approved", required = false) Optional<Boolean> areApproved) {
        if (username.equals("admin") && password.equals("admin"))
            if (areApproved.isPresent())
                return skinService.fetchAllApprovedSkins(areApproved.get());
            else
                return skinService.fetchAllSkins();
        return null;
    }

    @ApiOperation(value = "${AdminController.findSkinById}")
    @GetMapping(value = "/skin/{id}")
    public Skin findSkinById(@ApiParam("username") @RequestParam(name = "u") String username,
                             @ApiParam("password") @RequestParam(name = "p") String password,
                             @ApiParam("id") @PathVariable(name = "id") Long skinId) {
        if (username.equals("admin") && password.equals("admin"))
            return skinService.fetchSkinById(skinId);
        return null;
    }

    @ApiOperation(value = "${AdminController.resolveSkinById}")
    @GetMapping("/{action}/{id}")
    public HttpStatus resolveSkinById(@ApiParam("username") @RequestParam(name = "u") String username,
                                      @ApiParam("password") @RequestParam(name = "p") String password,
                                      @ApiParam("action") @PathVariable(name = "action") String action,
                                      @ApiParam("id") @PathVariable(name = "id") Long skinId) {
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
