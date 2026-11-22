package com.zerobear.UserService.scheduler;

import com.zerobear.UserService.dto.UserRegisterResponseEvent;
import com.zerobear.UserService.entity.UserOutBoxEvents;
import com.zerobear.UserService.enums.EventOutBoxStatus;
import com.zerobear.UserService.repository.UserOutBoxEventsRepository;
import com.zerobear.UserService.service.EventOutBoxService;
import com.zerobear.UserService.service.UserAcknowledgementProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class OutBoxEventAckScheduler {

    private final UserOutBoxEventsRepository userOutBoxEventsRepository;
    private final UserAcknowledgementProducerService producerService;
    private final EventOutBoxService eventOutBoxService;
    public OutBoxEventAckScheduler(UserOutBoxEventsRepository userEventsRepository, UserAcknowledgementProducerService producerService, EventOutBoxService eventOutBoxService) {
        this.userOutBoxEventsRepository = userEventsRepository;
        this.producerService = producerService;
        this.eventOutBoxService = eventOutBoxService;
    }

    @Scheduled(fixedRate = 5000)
    public void publishAckEvents(){
        List<UserOutBoxEvents> userEvents= userOutBoxEventsRepository.findAllByStatus(EventOutBoxStatus.PENDING);
        for(UserOutBoxEvents event:userEvents){
            UserRegisterResponseEvent registerEvent= new UserRegisterResponseEvent(event.getEventId(),event.getCorrelationId(),event.getUserId(),event.getRegisterationStatus(), event.getFailureReason());
            producerService.produce(registerEvent).whenComplete((result,error)->{
                if(error!=null){
                    log.error("Unable to publish Event {}",event.getEventId(),error);
                }else {
                    eventOutBoxService.updateStatus(event.getEventId(), EventOutBoxStatus.PUBLISH);
                }
            });
        }
    }
    private String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
