package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.RegisterRequest;
import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.exception.EmailAlreadyRegisteredException;
import com.zerobear.AuthService.exception.UserAlreadyExistsException;
import com.zerobear.AuthService.exception.UserNotFoundException;
import com.zerobear.AuthService.repositories.UserRepository;
import com.zerobear.AuthService.utility.ValidationUtility;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterService {

    private final ValidationUtility validationUtility;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEventProducerService userEventProducerService;

    public RegisterService(ValidationUtility validationUtility, PasswordEncoder passwordEncoder, UserRepository userRepository, UserEventProducerService userEventProducerService) {
        this.validationUtility = validationUtility;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userEventProducerService = userEventProducerService;
    }
    public Optional<User> getUserIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByName(userRegisterRequest.getName());
    }
    public Optional<User> findUserByEmailIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByEmail(userRegisterRequest.getEmail());
    }

    public boolean register(RegisterRequest userRegisterRequest) throws UserNotFoundException {
        validationUtility.validatePassword(userRegisterRequest.getPassword());
        validationUtility.validateEmail(userRegisterRequest.getEmail());

        String hashedPassword=passwordEncoder.encode(userRegisterRequest.getPassword());
        Optional<User> optionalUser= getUserIfExists(userRegisterRequest);
        Optional<User> optionalUserEmail = findUserByEmailIfExists(userRegisterRequest);
        if(optionalUserEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("User Email "+userRegisterRequest.getEmail()+" Already Registed .");
        }
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with username : " + userRegisterRequest.getName() + " already exists");
        }
        String id = UUID.randomUUID().toString();
        User user=  new User(id,userRegisterRequest.getName(),hashedPassword,new HashSet<>());
        User newUser=userRepository.save(user);
        UserRegisterEvent userRegisterEvent= new UserRegisterEvent(id,userRegisterRequest.getName(), userRegisterRequest.getPhoneNumber(), userRegisterRequest.getEmail());
        userEventProducerService.produceUserRegisterEvent("user-register",userRegisterEvent);
        return true;
    }



}
