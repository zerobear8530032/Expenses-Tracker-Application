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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidationUtility validationUtility;

    public UserService(UserRepository userRepository, ValidationUtility validationUtility) {
        this.userRepository = userRepository;
        this.validationUtility = validationUtility;
    }

    public UserResponse getUser(Long id){
        User user= userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Id "+id+" Does not exists "));
        UserResponse response = mapUserToUserResponse(user);
        return  response;
    }
    public UserResponse createUser(UserRegisterEvent userRegisterEvent) {
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
        user.setPhoneNumber(userRegisterEvent.getPhoneNumber());
        user.setUserId(userRegisterEvent.getUserId());
        User savedUser=userRepository.save(user);
        UserResponse userResponse= mapUserToUserResponse(savedUser);
        return userResponse;
    }
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        String name = updateUserRequest.getName();
        String email = updateUserRequest.getEmail();
        Long id = updateUserRequest.getId();
        validationUtility.validateEmail(email);
        validationUtility.validateUserName(name);
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Id "+id+" ooes not exists"));
        if (userRepository.existsByEmailAndIdNot(
                email, id)) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        if (userRepository.existsByNameAndIdNot(
                name, id)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhoneNumber(updateUserRequest.getPhoneNumber());

        User updatedUser=userRepository.save(user);
        return mapUserToUserResponse(updatedUser);
    }

    private UserResponse mapUserToUserResponse(User user){
        UserResponse userResponse= new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserId(user.getUserId());
        userResponse.setName(user.getName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        return  userResponse;
    }
}
