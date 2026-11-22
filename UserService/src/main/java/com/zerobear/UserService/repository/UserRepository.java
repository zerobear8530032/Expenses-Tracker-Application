package com.zerobear.UserService.repository;
import com.zerobear.UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    boolean existsByEmailAndUserIdNot(String email, String id);

    boolean existsByNameAndUserIdNot(String name, String id);

    public boolean existsByEventId(String eventId);
    public boolean existsByName(String eventId);
    public boolean existsByEmail(String eventId);

}
