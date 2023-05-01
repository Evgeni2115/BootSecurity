package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;

        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword("{noop}admin");
        admin.setRoles(Collections.singleton(Role.ROLE_ADMIN));
        userService.createUser(admin);
    }

    @GetMapping(value = "")
    public String index(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "all_users";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("user", new User());
        return "new_user";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        user.setRoles(roles);
        userService.createUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String getEdit(Model model, @PathVariable("id")Long id) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("editable_user") User user, @PathVariable("id") Long id) {

        userService.updateUser(id, user);
        return "redirect:/admin";
    }
}
