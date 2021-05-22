package com.example.kursach.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void send(String email, String subject, String message){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("javatemplates03@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject + ". Магазин компьютерных комплектующих \"JavaTemplates\"");
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
//        log.info("send() email:" + email
//                + " subject:" + subject
//                + " message:" + message + " message send");
    }
}
