package com.zerobear.UserService.service;

import com.zerobear.UserService.dto.UserRegisterEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    @KafkaListener(topics = "${kafka.events.user.topic}")
    public void consumeUserEvents(UserRegisterEvent userRegisterEvent) {

        System.out.println("Received user registration event: "
                + userRegisterEvent.getName());
    }
}