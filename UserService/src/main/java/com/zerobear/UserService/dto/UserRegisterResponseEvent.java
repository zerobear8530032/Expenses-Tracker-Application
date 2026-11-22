package com.zerobear.UserService.dto;

import com.zerobear.UserService.enums.ProfileStatus;
import com.zerobear.UserService.enums.RegistrationStatus;
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
