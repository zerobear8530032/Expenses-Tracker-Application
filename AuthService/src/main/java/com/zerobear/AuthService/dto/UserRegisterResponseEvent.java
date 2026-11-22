package com.zerobear.AuthService.dto;

import com.zerobear.AuthService.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterResponseEvent {
    String eventId;
    String correlationId;
    String userId;
    RegistrationStatus status;
    String failureReason;
}
