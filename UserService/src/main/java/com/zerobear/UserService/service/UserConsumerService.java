package com.zerobear.UserService.service;

import com.zerobear.UserService.dto.UserRegisterEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {


    private final UserService userService;

    public UserConsumerService(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "${kafka.events.user.topic}")
    public void consumeUserEvents(UserRegisterEvent userRegisterEvent) {
        try {
            userService.createUser(userRegisterEvent);
        } catch (Exception e) {
            System.out.println("Exception occurred "+e);
            System.out.println("Message : "+e.getMessage());

        }
    }
}