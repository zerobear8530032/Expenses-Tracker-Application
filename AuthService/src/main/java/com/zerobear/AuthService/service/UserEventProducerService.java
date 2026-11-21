package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.UserRegisterEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserEventProducerService {
    @Value("${kafka.events.user.topic}")
    String topic;
    private  final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate ;

    public UserEventProducerService(KafkaTemplate<String, UserRegisterEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, UserRegisterEvent>> produceUserRegisterEvent(
            UserRegisterEvent userRegisterEvent) {

        Message<UserRegisterEvent> message = MessageBuilder
                .withPayload(userRegisterEvent)
                .setHeader("event-type", "user-register-event")
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, userRegisterEvent.getUserId())
                .build();

        return kafkaTemplate.send(message);
    }
}
