package com.zerobear.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterEvent {
    String eventId;
    String userId;
    String name;
    String email;
}
