package com.zerobear.UserService.dto;

import com.zerobear.UserService.enums.ProfileStatus;

public class UserRegisterResponseEvent {
    String userId;
    ProfileStatus status;
    String message;

}
