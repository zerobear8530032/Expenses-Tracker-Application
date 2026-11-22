package com.zerobear.AuthService.entity;

import com.zerobear.AuthService.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "auth_idempotency"
)
public class Idempotency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true,nullable = false)
    String idempotencyKey;
    @Column(nullable = false)
    String requestHash;

    String responseBody;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    IdempotencyStatus status;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    HttpStatus httpStatus;
}
