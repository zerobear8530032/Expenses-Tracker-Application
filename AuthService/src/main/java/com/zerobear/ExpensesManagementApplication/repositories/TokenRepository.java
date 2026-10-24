package com.zerobear.ExpensesManagementApplication.repositories;

import com.zerobear.ExpensesManagementApplication.entity.RefreshToken;
import com.zerobear.ExpensesManagementApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
