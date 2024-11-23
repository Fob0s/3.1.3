package ru.kata.spring.boot_security.demo.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model) {
        model.addAttribute("allUser", userService.getAllUsers());
        return "admin/adminPage";
    }

    @GetMapping("/{id}")
    public String adminInfo(@PathVariable ("id") int id, Model model) {
        model.addAttribute("user", userService.findUserbyId(id));
        return "admin/userProfile";
    }
    @GetMapping("/new")
    public String createNewUser(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/createUser";
    }
    @PostMapping()
    public String createNewUserPost(@Valid @ModelAttribute("newUser") User user,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/createUser";
        }
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/update")
    public String updateUser(Model model, @PathVariable("id") int id) {
        System.out.println("RUN METHOD GGG");
        model.addAttribute("user", userService.findUserbyId(id));
        return "admin/updateUser";
    }

    @PostMapping("/{id}")
    public String updating(@Valid @ModelAttribute("user")  User user, BindingResult bindingResult,
                           @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "admin/updateUser";
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        if (userService.deleteUser(user.getId())){
            return "redirect:/admin";
        }else {
            return "redirect:/admin";
        }
    }
    @PostMapping("/{id}/addAdmin")
    public String addAdmin(@PathVariable("id") int id) {
        userService.addAdminRole(id);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/removeAdmin")
    public String removeAdmin(@PathVariable("id") int id) {
        userService.removeAdminRole(id);
        return "redirect:/admin";
    }

    /*
     дать админку / отнять админку / добавить во все страницы логаут
     */


}
