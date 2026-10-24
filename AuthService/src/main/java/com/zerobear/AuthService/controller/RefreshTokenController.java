package com.zerobear.AuthService.controller;

import com.zerobear.AuthService.dto.JwtTokenResponse;
import com.zerobear.AuthService.dto.RefreshTokenRequest;
import com.zerobear.AuthService.entity.RefreshToken;
import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.exception.RefrehTokenNotFoundException;
import com.zerobear.AuthService.service.JwtService;
import com.zerobear.AuthService.service.RefreshTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
