package com.zerobear.AuthService.repositories;

import com.zerobear.AuthService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public Optional<User> findByUserName(String name);

    Optional<User> findByEmail(String email);
}
