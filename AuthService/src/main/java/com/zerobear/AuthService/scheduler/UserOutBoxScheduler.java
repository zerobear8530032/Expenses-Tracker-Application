package com.zerobear.AuthService.scheduler;


import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import com.zerobear.AuthService.repositories.UserOutBoxRepository;
import com.zerobear.AuthService.service.UserEventProducerService;
import com.zerobear.AuthService.service.UserOutBoxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Component
public class UserOutBoxScheduler {
    private final UserOutBoxService userOutBoxService;
    private final UserEventProducerService userEventProducerService;
    private final UserOutBoxRepository userOutBoxRepository;
    private final ObjectMapper objectMapper;
    public UserOutBoxScheduler(UserOutBoxService userOutBoxService, UserEventProducerService userEventProducerService, UserOutBoxRepository repository, ObjectMapper objectMapper) {
        this.userOutBoxService = userOutBoxService;
        this.userEventProducerService = userEventProducerService;
        this.userOutBoxRepository = repository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 5000)
    public void userOutBoxPublisher(){
        List<UserOutBox> userOutBoxes = userOutBoxRepository.findByStatus(UserOutBoxStatus.PENDING);
        for(UserOutBox userOutBox : userOutBoxes){
            log.info("Publishing OutBox Entry : ");
            String json= userOutBox.getEventJson();
            UserRegisterEvent userRegisterEvent= toObject(json);
            userEventProducerService.produceUserRegisterEvent(userRegisterEvent).whenComplete((result,error)->{
                if(error!=null){
                    log.error("Unable to publish Event : {}",userOutBox.getId(),error);
                }else {
                    log.info("Published Event : {}",userOutBox.getId());
                    userOutBoxService.updateStatus(userOutBox.getId(),UserOutBoxStatus.PUBLISH);
                }
            });
        }

    }

    public UserRegisterEvent toObject(String json){
        try {
            log.info("Parsing Json to object :");
            UserRegisterEvent userRegisterEvent = objectMapper.readValue(json, UserRegisterEvent.class);
            return userRegisterEvent;
        }catch (JacksonException e){
            log.error("Json cannot be parsed {}",json,e);
            throw e;
        }

    }
}
