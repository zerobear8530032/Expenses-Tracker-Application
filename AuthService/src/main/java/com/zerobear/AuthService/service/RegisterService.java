package com.zerobear.AuthService.service;

import com.zerobear.AuthService.dto.RegisterRequest;
import com.zerobear.AuthService.dto.RegisterUserResponse;
import com.zerobear.AuthService.dto.UserRegisterEvent;
import com.zerobear.AuthService.entity.Idempotency;
import com.zerobear.AuthService.entity.Role;
import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.entity.UserOutBox;
import com.zerobear.AuthService.enums.AccountStatus;
import com.zerobear.AuthService.enums.IdempotencyStatus;
import com.zerobear.AuthService.enums.UserOutBoxStatus;
import com.zerobear.AuthService.exception.EmailAlreadyRegisteredException;
import com.zerobear.AuthService.exception.UserAlreadyExistsException;
import com.zerobear.AuthService.exception.UserNotFoundException;
import com.zerobear.AuthService.repositories.IdempotencyRepository;
import com.zerobear.AuthService.repositories.RoleRepository;
import com.zerobear.AuthService.repositories.UserOutBoxRepository;
import com.zerobear.AuthService.repositories.UserRepository;
import com.zerobear.AuthService.utility.ValidationUtility;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

@Service
public class RegisterService {

    private final ValidationUtility validationUtility;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserOutBoxRepository userOutBoxRepository;
    private final ObjectMapper objectMapper;
    private final IdempotencyRepository idempotencyRepository;
    public RegisterService(ValidationUtility validationUtility, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, UserOutBoxRepository userOutBoxRepository, ObjectMapper objectMapper, IdempotencyRepository idempotencyRepository) {
        this.validationUtility = validationUtility;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userOutBoxRepository = userOutBoxRepository;
        this.objectMapper = objectMapper;
        this.idempotencyRepository = idempotencyRepository;
    }
    public Optional<User> getUserIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByUserName(userRegisterRequest.getUserName());
    }
    public Optional<User> findUserByEmailIfExists(RegisterRequest userRegisterRequest) throws UserNotFoundException{
        return  userRepository.findByEmail(userRegisterRequest.getEmail());
    }
    @Transactional(rollbackFor = Exception.class)
    public RegisterUserResponse register(String idempotentKey,RegisterRequest userRegisterRequest) throws UserNotFoundException, NoSuchAlgorithmException {
        Optional<Idempotency> optionalIdempotency=idempotencyRepository.findByIdempotencyKey(idempotentKey);
        String hashRequest= hashRequest(userRegisterRequest);
        if(optionalIdempotency.isPresent()){
            Idempotency idempotency= optionalIdempotency.get();
            if(!hashRequest.equals(idempotency.getRequestHash())){
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            // Then validate status
            if (idempotency.getStatus() != IdempotencyStatus.COMPLETED) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }

            return objectMapper.readValue(
                    idempotency.getResponseBody(),
                    RegisterUserResponse.class
            );
        }
        Idempotency idempotency = new Idempotency();
        idempotency.setStatus(IdempotencyStatus.IN_PROGRESS);
        idempotency.setRequestHash(hashRequest);
        idempotency.setIdempotencyKey(idempotentKey);
        Idempotency savedIdempotency=idempotencyRepository.saveAndFlush(idempotency);

        validationUtility.validatePassword(userRegisterRequest.getPassword());
        validationUtility.validateEmail(userRegisterRequest.getEmail());
        validationUtility.validateUserName(userRegisterRequest.getUserName());

        Optional<User> optionalUser= getUserIfExists(userRegisterRequest);
        Optional<User> optionalUserEmail = findUserByEmailIfExists(userRegisterRequest);
        if(optionalUserEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("User Email "+userRegisterRequest.getEmail()+" Already Registed .");
        }
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with username : " + userRegisterRequest.getUserName() + " already exists");
        }
        String hashedPassword=passwordEncoder.encode(userRegisterRequest.getPassword());
        String id = UUID.randomUUID().toString();
        Role role = roleRepository.findByRole("USER")
                .orElseThrow(() ->
                        new RuntimeException("USER role not configured")
                );

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user=  new User(id,userRegisterRequest.getUserName(),userRegisterRequest.getEmail(),hashedPassword,roles, AccountStatus.PENDING);
        User createdUser=userRepository.save(user);
        RegisterUserResponse userResponse= new RegisterUserResponse(createdUser.getId(),createdUser.getUserName(),createdUser.getEmail());
        UserOutBox userOutBox= new UserOutBox();
        String eventId= generateUUID();
        UserRegisterEvent userRegisterEvent= new UserRegisterEvent(eventId,id,userRegisterRequest.getUserName(), userRegisterRequest.getEmail());
        String json = toJson(userRegisterEvent);
        userOutBox.setCreatedAt(Instant.now());
        userOutBox.setStatus(UserOutBoxStatus.PENDING);
        userOutBox.setEventJson(json);
        userOutBoxRepository.save(userOutBox);
        savedIdempotency.setHttpStatus(HttpStatus.ACCEPTED);
        savedIdempotency.setResponseBody(toJson(userResponse));
        savedIdempotency.setStatus(IdempotencyStatus.COMPLETED);
        idempotencyRepository.save(savedIdempotency);

        return userResponse;
    }

    private String generateUUID(){
        return UUID.randomUUID().toString();
    }

    private String toJson(Object o){
        return  objectMapper.writeValueAsString(o);
    }

    private String hashRequest(RegisterRequest registerRequest) throws NoSuchAlgorithmException {
        byte[] requestBytes=objectMapper.writeValueAsBytes(registerRequest);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestBytes);

        return HexFormat.of().formatHex(hash);
    }

}
