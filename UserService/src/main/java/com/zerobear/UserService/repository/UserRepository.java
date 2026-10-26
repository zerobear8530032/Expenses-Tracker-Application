package com.zerobear.UserService.repository;
import com.zerobear.UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByNameAndIdNot(String name, Long id);
}
