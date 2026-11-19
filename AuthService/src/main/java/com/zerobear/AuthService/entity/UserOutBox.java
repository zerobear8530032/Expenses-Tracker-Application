package com.zerobear.AuthService.entity;

import com.zerobear.AuthService.enums.UserOutBoxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserOutBox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Enumerated(EnumType.STRING)
    UserOutBoxStatus status;
    @Column(columnDefinition = "JSON")
    String eventJson;
    Instant createdAt;

}
