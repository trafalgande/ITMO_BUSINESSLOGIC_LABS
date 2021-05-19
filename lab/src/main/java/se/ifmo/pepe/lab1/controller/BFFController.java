package se.ifmo.pepe.lab1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.service.UserService;

import java.util.List;

@RestController
@Slf4j
public class BFFController {
    private final UserService userService;

    @Autowired
    public BFFController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("")
    public ModelAndView index() {
        List<User> authors = userService.fetchAllAuthors();
        log.info("Fetching authors: {}", authors);
        ModelAndView model = new ModelAndView();
        model.setViewName("/index.html");
        model.addObject("authors", authors);
        return model;
    }
}
