package com.example.kursach.services;

import com.example.kursach.models.Item;
import com.example.kursach.models.Order;
import com.example.kursach.models.User;
import com.example.kursach.repositories.OrderRepository;
import com.example.kursach.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    public void addOrder(Order order, User user){
        order.setUser(user);
        order.setPrice((double) 0);
        order.setItems(new ArrayList<>());
        orderRepository.save(order);
    //    log.info("addOrder() order:" + order + " success");
    }

    public void deleteOrder(Long id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()){
            orderRepository.deleteById(id);
    //        log.info("deleteOrder() id:" + id + " success");
            return;
        }
    //    log.info("deleteOrder() id:" + id + " can't delete: order not found");
    }

    public String finishOrder(Long id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.get().getItems().isEmpty()){
            //        log.info("finishOrder() id:" + id + " can't finish: cart is empty");
            return "Невозможно оформить заказ: корзина пуста";
        }
        else if(orderOptional.isPresent()){
            orderOptional.get().setFinished(true);
    //        log.info("finishOrder() id:" + id + " order is finished");
            User user = orderOptional.get().getUser();
            addOrder(new Order() ,user);
            sendMakeOrderMessage(user, orderOptional.get());
            return "Заказ №" + orderOptional.get().getId() + " успешно оформлен";
        }
    //    log.info("finishOrder() id:" + id + " can't finish: order not found");
        return "Заказ не найден";
    }

    public List<Order> findAll(){
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    public Order findById(Long id){
        Optional<Order> orderOptional = orderRepository.findById(id);
    //    log.info("findById() id:" + id + " found:" + orderOptional);
        return orderOptional.get();
    }

    public Order findCartByUser(User user){
        Order cart = orderRepository.findByUserAndIsFinished(user, false);
    //    log.info("findCartByUser() user:" + user + " found:" + cart);
        return cart;
    }

    public List<Order> findAllByUser(User user){
        List<Order> orders = orderRepository.findAllByUserAndIsFinished(user, true);
        return orders;
    }

    public void addItemToCart(User user, Item item){
        Order cart = orderRepository.findByUserAndIsFinished(user, false);
        if(cart == null){
            cart = new Order();
            this.addOrder(cart, user);
        }
        cart.getItems().add(item);
        Double price = cart.getPrice() + item.getPrice();
        cart.setPrice(price);
    //    log.info("addItemToCart() user:" + user + " item:" + item + " added");
    }

    public void removeItemFromCart(User user, Item item){
        Order cart = orderRepository.findByUserAndIsFinished(user, false);
        cart.getItems().remove(item);
        cart.setPrice(cart.getPrice() - item.getPrice());
    //    log.info("removeItemFromCart() user:" + user + " item:" + item + " removed");
    }

    private void sendMakeOrderMessage(User user, Order order){
        if(user.getEmail() != null){
            String message = user.getUsername() + ", вы успешно оформили заказ #" + order.getId()
                    + " итоговой стоимостью: " + order.getPrice() + ".\n"
                    + "Товары:\nНаименование\tЦена\n";
            for (Item item: order.getItems()){
                message += item.getName() + "\t" + item.getPrice() + "\n";
            }
            emailService.send(user.getEmail(), "Оформление заказа #" + order.getId(), message);
            //        log.info("sendMakeOrderMessage() user:" + user + " order:" + order + " success");
            return;
        }
        //    log.info("sendMakeOrderMessage() user:" + user + " order:" + order + " can't send: email not found");
    }
}
