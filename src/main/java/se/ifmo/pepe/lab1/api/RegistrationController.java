package se.ifmo.pepe.lab1.api;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.ifmo.pepe.lab1.model.Role;
import se.ifmo.pepe.lab1.model.User;
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
        roleService.saveRole(new Role().setName("ROLE_USER"));
        roleService.saveRole(new Role().setName("ROLE_ADMIN"));
    }


//    @GetMapping("/register")
//    public String registration(Model model) {
//        model.addAttribute("userForm", new User());
//        return "registration";
//    }

    @PostMapping("/signup")
    public Boolean addUser(@ApiParam("username") @RequestParam(name = "username") String username,
                           @ApiParam("password") @RequestParam(name = "password") String password) {
        System.out.println(username + " " + password);
        return userService.saveUser(new User()
                .setUsername(username)
                .setPassword(password));
    }
}
