package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.UserRegisterResponseEvent;
import com.zerobear.AuthService.service.UserAcknowledgementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAcknowledgementConsumer {

    private final UserAcknowledgementService acknowledgementService;

    public UserAcknowledgementConsumer(
            UserAcknowledgementService acknowledgementService
    ) {
        this.acknowledgementService = acknowledgementService;
    }

    @KafkaListener(
            topics = "${kafka.user.ack.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(UserRegisterResponseEvent event) {

        log.info(
                "Received registration acknowledgement. EventId: {}, UserId: {}",
                event.getEventId(),
                event.getUserId()
        );

        acknowledgementService.processAcknowledgement(event);

        log.info(
                "Successfully processed registration acknowledgement: {}",
                event.getEventId()
        );
    }
}