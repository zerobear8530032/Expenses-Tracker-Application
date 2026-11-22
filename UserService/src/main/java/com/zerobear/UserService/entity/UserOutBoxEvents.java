package com.zerobear.UserService.entity;

import com.zerobear.UserService.enums.RegistrationStatus;
import com.zerobear.UserService.enums.EventOutBoxStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserOutBoxEvents {
    @Id
    String eventId;
    @Column(nullable = false)
    String correlationId;
    @Column(nullable = false)
    String userId;
    @Enumerated(EnumType.STRING)
    RegistrationStatus RegisterationStatus;
    @Column(nullable = true)
    String failureReason;
    @Enumerated(EnumType.STRING)
    EventOutBoxStatus status;
}
