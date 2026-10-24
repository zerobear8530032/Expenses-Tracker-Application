package com.zerobear.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterEvent {
    String name;
    String password;
    String phoneNumber;
    String email;
}
