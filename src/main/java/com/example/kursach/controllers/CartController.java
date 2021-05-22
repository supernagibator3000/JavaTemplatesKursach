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
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping
    public String getAll(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("cart", orderService.findCartByUser(user));
        return "cart";
    }

    @PostMapping
    public String deleteItemFromCart(@RequestParam(defaultValue = "") Long itemId,
                                     @RequestParam(defaultValue = "") String action,
                                     @AuthenticationPrincipal User user,
                                     Model model){
        if(action.equals("deleteFromCart")){
            orderService.removeItemFromCart(user, itemService.findById(itemId));
        }
        else if(action.equals("makeOrder")){
            String orderResult = orderService.finishOrder(orderService.findCartByUser(user).getId());
            model.addAttribute("orderResult", orderResult);
        }
        model.addAttribute("cart", orderService.findCartByUser(user));
        return "cart";
    }
}
