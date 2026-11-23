package com.zerobear.AuthService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "processed_user_acknowledgements",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_processed_ack_event_id",
            columnNames = "event_id"
        )
    }
)
public class ProcessedUserAcknowledgement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private Instant processedAt;
}