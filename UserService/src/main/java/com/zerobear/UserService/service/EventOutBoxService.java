package com.zerobear.UserService.service;

import com.zerobear.UserService.entity.UserOutBoxEvents;
import com.zerobear.UserService.enums.EventOutBoxStatus;
import com.zerobear.UserService.exception.EventNotFoundException;
import com.zerobear.UserService.repository.UserOutBoxEventsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventOutBoxService {

    private final UserOutBoxEventsRepository userOutBoxEventsRepository;

    public EventOutBoxService(UserOutBoxEventsRepository userEventsRepository) {
        this.userOutBoxEventsRepository = userEventsRepository;
    }

    public List<UserOutBoxEvents> getAllEvents(){
        return userOutBoxEventsRepository.findAll();
    }
    @Transactional
    public void updateStatus(String eventId, EventOutBoxStatus status){
        UserOutBoxEvents userOutBoxEvents= userOutBoxEventsRepository.findByEventId(eventId).orElseThrow(()-> new EventNotFoundException("eventId "+eventId+"  does not exists"));
        userOutBoxEvents.setStatus(status);
        userOutBoxEventsRepository.save(userOutBoxEvents);
    }
}
