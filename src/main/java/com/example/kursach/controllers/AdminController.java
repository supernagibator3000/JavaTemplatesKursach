package com.example.kursach.controllers;

import com.example.kursach.models.Item;
import com.example.kursach.services.ItemService;
import com.example.kursach.services.OrderService;
import com.example.kursach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @GetMapping
    public String getUsersAndItems(Model model){
        model.addAttribute("users", userService.findAll());
        model.addAttribute("items", itemService.findAll());
        model.addAttribute("orders", orderService.findAll());
        return "admin";
    }

    @PostMapping
    public String addAndDel(@RequestParam(defaultValue = "название_товара") String name,
                            @RequestParam(defaultValue = "0.0") Double price,
                            @RequestParam(defaultValue = "описание_товара") String description,
                            @RequestParam(defaultValue = "") Long userId,
                            @RequestParam(defaultValue = "") Long itemId,
                            @RequestParam(defaultValue = "") String action){
        if(action.equals("addItem")){
            itemService.addItem(new Item(name, price, description));
        }
        else if(action.equals("deleteItem")){
            itemService.deleteItem(itemId);
        }
        else if(action.equals("deleteUser")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin";
    }
}
