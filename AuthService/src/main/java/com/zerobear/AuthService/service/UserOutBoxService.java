package com.zerobear.AuthService.service;

import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import com.zerobear.AuthService.repositories.UserOutBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserOutBoxService {

    private final UserOutBoxRepository userOutBoxRepository;

    public UserOutBoxService(UserOutBoxRepository userOutBoxRepository) {
        this.userOutBoxRepository = userOutBoxRepository;
    }


    public List<UserOutBox> findByStatus(UserOutBoxStatus status){
        return userOutBoxRepository.findByStatus(status);
    }

    @Transactional
    public void updateStatus(Long id,UserOutBoxStatus status){

        UserOutBox user = userOutBoxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outbox entry not found"));
        user.setStatus(status);
        userOutBoxRepository.save(user);
    }
}
