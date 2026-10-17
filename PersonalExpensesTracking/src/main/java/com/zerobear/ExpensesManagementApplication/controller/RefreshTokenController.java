package com.zerobear.ExpensesManagementApplication.controller;

import com.zerobear.ExpensesManagementApplication.dto.JwtTokenResponse;
import com.zerobear.ExpensesManagementApplication.dto.RefreshTokenRequest;
import com.zerobear.ExpensesManagementApplication.entity.RefreshToken;
import com.zerobear.ExpensesManagementApplication.entity.User;
import com.zerobear.ExpensesManagementApplication.exception.RefrehTokenNotFoundException;
import com.zerobear.ExpensesManagementApplication.service.JwtService;
import com.zerobear.ExpensesManagementApplication.service.RefreshTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public RefreshTokenController(RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("auth/v1/refreshToken")
    public JwtTokenResponse refreshToken(@RequestBody RefreshTokenRequest tokenRequest){
        RefreshToken refreshToken= refreshTokenService.findByToken(tokenRequest.getRefreshToken()).orElseThrow(()-> new RefrehTokenNotFoundException("Token not present in the database"));
        refreshTokenService.isExpired(refreshToken);
        User user= refreshToken.getUser();
        String jwt = jwtService.createToken(user.getName());
        JwtTokenResponse response= new JwtTokenResponse(jwt,refreshToken.getToken());
        return response;
    }
}
