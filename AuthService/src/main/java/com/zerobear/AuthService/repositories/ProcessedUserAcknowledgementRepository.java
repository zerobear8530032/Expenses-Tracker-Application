package com.zerobear.AuthService.repositories;

import com.zerobear.AuthService.entity.ProcessedUserAcknowledgement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedUserAcknowledgementRepository
        extends JpaRepository<ProcessedUserAcknowledgement, Long> {

    boolean existsByEventId(String eventId);
}