package com.zerobear.UserService.service;

import com.zerobear.UserService.dto.UpdateUserRequest;
import com.zerobear.UserService.dto.UserRegisterEvent;
import com.zerobear.UserService.dto.UserResponse;
import com.zerobear.UserService.entity.User;
import com.zerobear.UserService.exception.EmailAlreadyRegisteredException;
import com.zerobear.UserService.exception.UserAlreadyExistsException;
import com.zerobear.UserService.exception.UserNotFoundException;
import com.zerobear.UserService.repository.UserRepository;
import com.zerobear.UserService.utility.ValidationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidationUtility validationUtility;

    public UserService(UserRepository userRepository, ValidationUtility validationUtility) {
        this.userRepository = userRepository;
        this.validationUtility = validationUtility;
    }

    public UserResponse getUser(String id){
        User user= userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Id "+id+" Does not exists "));
        UserResponse response = mapUserToUserResponse(user);
        return  response;
    }
    @Transactional
    public void createUser(UserRegisterEvent userRegisterEvent) {

        if (userRepository.existsByEventId(userRegisterEvent.getEventId())) {
            log.info("Duplicate event received: {}", userRegisterEvent.getEventId());
            return;
        }
        String name = userRegisterEvent.getName();
        String email = userRegisterEvent.getEmail();

        Optional<User> optionalUserName = userRepository.findByName(name);
        if(optionalUserName.isPresent()){
            throw new UserAlreadyExistsException("User Name : "+name+" Already register");
        }
        Optional<User> optionalUserEmail = userRepository.findByEmail(email);
        if(optionalUserEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("Email : "+email+" Already register");
        }

        User user= new User();
        user.setName(name);
        user.setEmail(email);
        user.setEventId(userRegisterEvent.getEventId());
        user.setUserId(userRegisterEvent.getUserId());
        User savedUser=userRepository.save(user);

    }
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        String name = updateUserRequest.getName();
        String email = updateUserRequest.getEmail();
        String id = updateUserRequest.getId();
        validationUtility.validateEmail(email);
        validationUtility.validateUserName(name);
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Id "+id+" ooes not exists"));
        if (userRepository.existsByEmailAndUserIdNot(
                email, id)) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        if (userRepository.existsByNameAndUserIdNot(
                name, id)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());

        User updatedUser=userRepository.save(user);
        return mapUserToUserResponse(updatedUser);
    }

    private UserResponse mapUserToUserResponse(User user){
        UserResponse userResponse= new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        return  userResponse;
    }
}
