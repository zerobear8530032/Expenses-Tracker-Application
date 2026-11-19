package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.RegisterRequest;
import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.entity.Role;
import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import com.zerobear.AuthService.exception.EmailAlreadyRegisteredException;
import com.zerobear.AuthService.exception.UserAlreadyExistsException;
import com.zerobear.AuthService.exception.UserNotFoundException;
import com.zerobear.AuthService.repositories.RoleRepository;
import com.zerobear.AuthService.repositories.UserOutBoxRepository;
import com.zerobear.AuthService.repositories.UserRepository;
import com.zerobear.AuthService.utility.ValidationUtility;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RegisterService {

    private final ValidationUtility validationUtility;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEventProducerService userEventProducerService;
    private final RoleRepository roleRepository;
    private final UserOutBoxRepository userOutBoxRepository;
    private final ObjectMapper objectMapper;
    public RegisterService(ValidationUtility validationUtility, PasswordEncoder passwordEncoder, UserRepository userRepository, UserEventProducerService userEventProducerService, RoleRepository roleRepository, UserOutBoxRepository userOutBoxRepository, ObjectMapper objectMapper) {
        this.validationUtility = validationUtility;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userEventProducerService = userEventProducerService;
        this.roleRepository = roleRepository;
        this.userOutBoxRepository = userOutBoxRepository;
        this.objectMapper = objectMapper;
    }
    public Optional<User> getUserIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByUserName(userRegisterRequest.getUserName());
    }
    public Optional<User> findUserByEmailIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByEmail(userRegisterRequest.getEmail());
    }
    @Transactional
    public boolean register(RegisterRequest userRegisterRequest) throws UserNotFoundException {
        validationUtility.validatePassword(userRegisterRequest.getPassword());
        validationUtility.validateEmail(userRegisterRequest.getEmail());
        validationUtility.validateUserName(userRegisterRequest.getUserName());

        String hashedPassword=passwordEncoder.encode(userRegisterRequest.getPassword());
        Optional<User> optionalUser= getUserIfExists(userRegisterRequest);
        Optional<User> optionalUserEmail = findUserByEmailIfExists(userRegisterRequest);
        if(optionalUserEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("User Email "+userRegisterRequest.getEmail()+" Already Registed .");
        }
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with username : " + userRegisterRequest.getUserName() + " already exists");
        }
        String id = UUID.randomUUID().toString();
        Role role = roleRepository.findByRole("USER")
                .orElseThrow(() ->
                        new RuntimeException("USER role not configured")
                );

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user=  new User(id,userRegisterRequest.getUserName(),userRegisterRequest.getEmail(),hashedPassword,roles);
        User createdUser=userRepository.save(user);
        UserOutBox userOutBox= new UserOutBox();

        UserRegisterEvent userRegisterEvent= new UserRegisterEvent(id,userRegisterRequest.getUserName(), userRegisterRequest.getEmail());
        String json = toJson(userRegisterEvent);
        userOutBox.setCreatedAt(Instant.now());
        userOutBox.setStatus(UserOutBoxStatus.PENDING);
        userOutBox.setEventJson(json);
        userOutBoxRepository.save(userOutBox);
//        userEventProducerService.produceUserRegisterEvent(id,userRegisterEvent);
        return true;
    }

    private String toJson(UserRegisterEvent userRegisterEvent){
        return objectMapper.writeValueAsString(userRegisterEvent);
    }





}
