package com.zerobear.AuthService.scheduler;


import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import com.zerobear.AuthService.repositories.UserOutBoxRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class UserOutBoxScheduler {

    private final UserOutBoxRepository repository;

    public UserOutBoxScheduler(UserOutBoxRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 5000)
    public void userOutBoxPublisher(){
        List<UserOutBox> userOutBoxes = repository.findByStatus(UserOutBoxStatus.PENDING);
        System.out.println(userOutBoxes);
    }
}
