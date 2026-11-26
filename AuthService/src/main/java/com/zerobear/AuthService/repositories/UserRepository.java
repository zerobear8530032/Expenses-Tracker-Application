package com.zerobear.AuthService.repositories;

import com.zerobear.AuthService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public Optional<User> findByUserName(String name);


    @Query("""
    SELECT DISTINCT u
    FROM User u
    LEFT JOIN FETCH u.roles
    WHERE u.userName = :username
    """)
    Optional<User> findByNameWithRoles(@Param("username") String name);
}
