package com.zerobear.ExpensesManagementApplication.service;

import com.zerobear.ExpensesManagementApplication.entity.RefreshToken;
import com.zerobear.ExpensesManagementApplication.entity.User;
import com.zerobear.ExpensesManagementApplication.repositories.TokenRepository;
import com.zerobear.ExpensesManagementApplication.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

   private final  TokenRepository tokenRepository;
   private final UserRepository userRepository;

    public RefreshTokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }


    public RefreshToken createToken(String username) {
        User user = userRepository.findByName(username).orElseThrow(()-> new UsernameNotFoundException("User name "+username+"  not found in database"));
        RefreshToken refreshToken= tokenRepository.findByUser(user).orElseGet(RefreshToken::new);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(new Date().toInstant().plusMillis(600000));
        refreshToken.setToken(UUID.randomUUID().toString());
        return tokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
    public RefreshToken isExpired(RefreshToken token){
        if(token.getExpiryDate().compareTo(new Date().toInstant())<0){
            tokenRepository.delete(token);
            throw new RuntimeException("Token "+token.getToken()+" is expired");
        }
        return token;
    }

}
