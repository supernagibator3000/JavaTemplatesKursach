package com.example.kursach.services;


import com.example.kursach.models.Order;
import com.example.kursach.models.Role;
import com.example.kursach.models.User;
import com.example.kursach.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    OrderService orderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
    //    else
    //        log.info("loadUserByUsername() username:" + username + " found:" + user);
        return user;
    }

    public List<User> findAll(){
        List<User> users = userRepository.findAll();
   //     log.info("findAll() found:" + users);
        return users;
    }

    public String addUser(User user){
        if(userRepository.findByUsername(user.getUsername()) != null){
    //        log.info("addUser() user:" + user.getId() + " can't add: user with this username already exists");
            return "Пользователь с таким именем уже существует";
        }
        if(userRepository.findByEmail(user.getEmail()) != null){
    //        log.info("addUser() user:" + user.getId() + " can't add: user with this email already exists");
            return "Пользователь с таким Email уже существует";
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        orderService.addOrder(new Order(), user);
    //    log.info("addUser() user:" + user + " success");
        sendSignUpMessage(user);
        return "";
    }

    public void deleteUser(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            userRepository.deleteById(id);
    //        log.info("deleteUser() id:" + id + " success");
            return;
        }
    //    log.info("deleteUser() id:" + id + " can't delete: user not found");
    }

    public User findById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
    //    log.info("findById() id:" + id + " found:" + userOptional);
        return userOptional.get();
    }

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username);
    //    log.info("findById() username:" + username + " found:" + user);
        return user;
    }

    private void sendSignUpMessage(User user){
        if(user.getEmail() != null){
            String message = user.getUsername() + ", поздравляем с успешной регистрацией аккаунта!\n";
            emailService.send(user.getEmail(), "Регистрация аккаунта", message);
    //        log.info("sendSignUpMessage() user:" + user + " success");
            return;
        }
    //    log.info("sendSignUpMessage() user:" + user + " can't send: email not found");
    }
}
