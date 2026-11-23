package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.UserRegisterResponseEvent;
import com.zerobear.AuthService.entity.ProcessedUserAcknowledgement;
import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.enums.AccountStatus;
import com.zerobear.AuthService.enums.RegistrationStatus;
import com.zerobear.AuthService.repositories.ProcessedUserAcknowledgementRepository;
import com.zerobear.AuthService.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserAcknowledgementService {

    private final UserRepository userRepository;
    private final ProcessedUserAcknowledgementRepository acknowledgementRepository;

    public UserAcknowledgementService(
            UserRepository userRepository,
            ProcessedUserAcknowledgementRepository acknowledgementRepository
    ) {
        this.userRepository = userRepository;
        this.acknowledgementRepository = acknowledgementRepository;
    }

    @Transactional
    public void processAcknowledgement(UserRegisterResponseEvent event) {

        // 1. Validate incoming event
        if (event == null ||
                event.getEventId() == null ||
                event.getCorrelationId() == null ||
                event.getUserId() == null ||
                event.getStatus() == null) {
            throw new IllegalArgumentException("Invalid acknowledgement event");
        }

        // 2. Check whether this acknowledgement was already processed
        if (acknowledgementRepository.existsByEventId(event.getEventId())) {
            return;
        }

        // 3. Find the corresponding AuthService user
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User not found: " + event.getUserId()
                        )
                );


        // 4. Update account status
        AccountStatus newStatus;

        if (event.getStatus() == RegistrationStatus.SUCCESS) {
            newStatus = AccountStatus.CREATED;
        } else {
            newStatus = AccountStatus.FAILED;
        }

        // Do not allow a different acknowledgement to reverse a terminal state
        if (user.getStatus() != AccountStatus.PENDING) {

            if (user.getStatus() != newStatus) {
                throw new IllegalStateException(
                        "Conflicting registration acknowledgement for user: "
                                + user.getId()
                );
            }

        } else {
            user.setStatus(newStatus);
            userRepository.save(user);
        }

        // 6. Record processed acknowledgement
        ProcessedUserAcknowledgement processed =
                new ProcessedUserAcknowledgement();

        processed.setEventId(event.getEventId());
        processed.setCorrelationId(event.getCorrelationId());
        processed.setProcessedAt(Instant.now());

        acknowledgementRepository.saveAndFlush(processed);
    }
}