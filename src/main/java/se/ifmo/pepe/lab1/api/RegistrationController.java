package se.ifmo.pepe.lab1.api;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.ifmo.pepe.lab1.model.Role;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.model.Wallet;
import se.ifmo.pepe.lab1.service.RoleService;
import se.ifmo.pepe.lab1.service.UserService;

import javax.annotation.PostConstruct;

@RestController
public class RegistrationController {


    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void init() {
        String[] roles = {"ROLE_USER", "ROLE_ADMIN"};
        for (String role : roles)
            if (!roleService.fetchRoleByName(role).isPresent())
                roleService.saveRole(new Role().setName(role));
    }


    @PostMapping("/signup")
    public HttpStatus addUser(@ApiParam("username") @RequestParam(name = "username") String username,
                              @ApiParam("password") @RequestParam(name = "password") String password) {
        if (userService.saveUser(new User()
                .setUsername(username)
                .setPassword(password)
                .setWallet(new Wallet())))
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }
}
