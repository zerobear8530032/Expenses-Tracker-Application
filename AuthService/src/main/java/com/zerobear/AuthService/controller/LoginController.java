package com.zerobear.AuthService.controller;

import com.zerobear.AuthService.dto.AuthenticationResponse;
import com.zerobear.AuthService.dto.JwtTokenResponse;
import com.zerobear.AuthService.dto.LoginRequest;
import com.zerobear.AuthService.entity.RefreshToken;
import com.zerobear.AuthService.service.CustomUserDetails;
import com.zerobear.AuthService.service.JwtService;
import com.zerobear.AuthService.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.Utilities;

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

    @GetMapping
    public ResponseEntity<AuthenticationResponse> authenticateRequest(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthenticationResponse(userDetails.getUserId()));
    }
}
