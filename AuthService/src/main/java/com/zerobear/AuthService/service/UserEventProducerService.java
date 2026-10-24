package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.UserRegisterEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducerService {
    @Value("${kafka.events.user.topic}")
    String topic;
    private  final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate ;

    public UserEventProducerService(KafkaTemplate<String, UserRegisterEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceUserRegisterEvent(
            String key,
            UserRegisterEvent userRegisterEvent) {

        Message<UserRegisterEvent> message = MessageBuilder
                .withPayload(userRegisterEvent)
                .setHeader("event-type", "user-register-event")
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        kafkaTemplate.send(message);
    }
}
