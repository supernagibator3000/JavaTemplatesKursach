package com.example.kursach.controllers;

import com.example.kursach.models.User;
import com.example.kursach.services.ItemService;
import com.example.kursach.services.OrderService;
import com.example.kursach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping
    public String getAllItems(Model model){
        model.addAttribute("items", itemService.findAll());
        return "shop";
    }

    @PostMapping
    public String addItemToCart(@RequestParam(defaultValue = "") Long itemId,
                                @RequestParam(defaultValue = "") String action,
                                @AuthenticationPrincipal User user){
        if(action.equals("addToCart")){
            orderService.addItemToCart(user, itemService.findById(itemId));
        }
        return "redirect:/shop";
    }
}
