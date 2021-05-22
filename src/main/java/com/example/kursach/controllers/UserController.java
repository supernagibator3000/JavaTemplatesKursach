package com.example.kursach.controllers;

import com.example.kursach.models.User;
import com.example.kursach.services.OrderService;
import com.example.kursach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/signUp")
    public String signUp(){
        return "sign_up";
    }

    @PostMapping("/signUp")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "sign_up";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "sign_up";
        }
        String answer = userService.addUser(user);
        if (!answer.equals("")){
            model.addAttribute("usernameError", answer);
            return "sign_up";
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("orders", orderService.findAllByUser(user));
        return "profile";
    }
}
