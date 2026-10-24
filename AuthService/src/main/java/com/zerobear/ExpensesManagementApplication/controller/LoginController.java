package com.zerobear.ExpensesManagementApplication.controller;

import com.zerobear.ExpensesManagementApplication.dto.JwtTokenResponse;
import com.zerobear.ExpensesManagementApplication.dto.LoginRequest;
import com.zerobear.ExpensesManagementApplication.entity.RefreshToken;
import com.zerobear.ExpensesManagementApplication.service.JwtService;
import com.zerobear.ExpensesManagementApplication.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    private  final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Autowired
    public LoginController(AuthenticationProvider authenticationProvider, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/auth/v1/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(),loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken= refreshTokenService.createToken(loginRequest.getName());
            JwtTokenResponse jwtTokenResponse= new JwtTokenResponse(jwtService.createToken(loginRequest.getName()),refreshToken.getToken());
            return  new ResponseEntity(jwtTokenResponse, HttpStatus.OK);
        }else{
            return  new ResponseEntity("Some thing went wrong unable to login",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
