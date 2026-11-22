package com.zerobear.AuthService.dto;

import com.zerobear.AuthService.enums.AccountStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterUserResponse {
    private String userId;
    private String userName;
    private String email;
}
