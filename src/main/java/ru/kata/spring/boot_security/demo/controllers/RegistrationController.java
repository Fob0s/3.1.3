package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("newUser", new User());
        return "registration/registration";
    }

    @PostMapping("/registration")
    public String registrationPostMapping(@Valid @ModelAttribute("newUser") User user,
                                          BindingResult bindingResult) {
        Arrays.stream(bindingResult.getSuppressedFields()).forEach(System.out::println);

        if (bindingResult.hasErrors()) {
            System.out.println("has eror");
            return "registration/registration";
        }


        userService.createUser(user);
        return "redirect:/index";
    }
}
