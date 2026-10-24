package com.zerobear.ExpensesManagementApplication.controller;

import com.zerobear.ExpensesManagementApplication.dto.JwtTokenResponse;
import com.zerobear.ExpensesManagementApplication.dto.RegisterRequest;
import com.zerobear.ExpensesManagementApplication.entity.RefreshToken;
import com.zerobear.ExpensesManagementApplication.exception.InvalidEmailException;
import com.zerobear.ExpensesManagementApplication.exception.InvalidPasswordException;
import com.zerobear.ExpensesManagementApplication.exception.UserAlreadyExistsException;
import com.zerobear.ExpensesManagementApplication.service.JwtService;
import com.zerobear.ExpensesManagementApplication.service.RegisterService;
import com.zerobear.ExpensesManagementApplication.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    private final RegisterService registerService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public RegisterController(RegisterService registerService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.registerService = registerService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/v1/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest){
        try{
            boolean signUpUser = registerService.register(registerRequest);
            RefreshToken refreshToken = refreshTokenService.createToken(registerRequest.getName());
            String jwtToken = jwtService.createToken(Map.of(),registerRequest.getName());
            JwtTokenResponse response = new JwtTokenResponse(jwtToken,refreshToken.getToken());
            return  new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (UserAlreadyExistsException ex){
            return  new ResponseEntity<>("User Already Exists", HttpStatus.BAD_REQUEST);
        }catch (InvalidEmailException | InvalidPasswordException ex){
           return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            return  new ResponseEntity<>("Some Error occurr",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
