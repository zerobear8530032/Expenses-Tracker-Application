package com.zerobear.UserService.service;

import com.zerobear.UserService.dto.UserRegisterResponseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserAcknowledgementProducerService {

    @Value("${kafka.user.ack.topic}")
    String topic;
    private final KafkaTemplate<String, UserRegisterResponseEvent> kafkaTemplate;

    public UserAcknowledgementProducerService(KafkaTemplate<String, UserRegisterResponseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, UserRegisterResponseEvent>> produce(UserRegisterResponseEvent userRegisterResponseEvent){
        return kafkaTemplate.send(topic,userRegisterResponseEvent.getUserId(),userRegisterResponseEvent);
    }
}
