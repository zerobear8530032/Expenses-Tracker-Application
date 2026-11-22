package com.zerobear.UserService.repository;

import com.zerobear.UserService.entity.UserOutBoxEvents;
import com.zerobear.UserService.enums.EventOutBoxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOutBoxEventsRepository extends JpaRepository<UserOutBoxEvents,String> {
    Optional<UserOutBoxEvents> findByEventId(String eventId);
    List<UserOutBoxEvents> findAllByStatus(EventOutBoxStatus status);
}
