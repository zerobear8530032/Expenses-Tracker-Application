package com.zerobear.AuthService.repositories;


import com.zerobear.AuthService.entity.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<Idempotency,Long> {
    Optional<Idempotency>
    findByIdempotencyKey(
            String idempotencyKey
    );
}
