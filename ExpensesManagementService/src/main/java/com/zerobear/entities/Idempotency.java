package com.zerobear.entities;

import com.zerobear.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "expenses_processes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_idempotency_key",
                        columnNames = {"user_id", "idempotency_key"}
                )
        }
)
public class Idempotency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String idempotencyKey;
    String userId;
    String requestHash;
    @Column(columnDefinition = "JSON")
    String requestBody;
    String responseBody;
    @Enumerated(EnumType.STRING)
    IdempotencyStatus status;
    @Enumerated(EnumType.STRING)
    HttpStatus httpStatus;
}
