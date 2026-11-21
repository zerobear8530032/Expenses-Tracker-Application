package com.zerobear.UserService.dto;

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
