package com.zerobear.AuthService.repositories;

import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOutBoxRepository extends JpaRepository<UserOutBox,Long> {
    public List<UserOutBox> findByStatus(UserOutBoxStatus status);
}
