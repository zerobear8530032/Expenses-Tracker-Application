package com.zerobear.repository;

import com.zerobear.entities.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<Idempotency,Long> {

    Optional<Idempotency>
    findByUserIdAndIdempotencyKey(
            String userId,
            String idempotencyKey
    );
}
