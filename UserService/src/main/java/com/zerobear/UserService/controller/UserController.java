package com.zerobear.UserService.controller;

import com.zerobear.UserService.dto.UpdateUserRequest;
import com.zerobear.UserService.dto.UserRegisterRequest;
import com.zerobear.UserService.dto.UserResponse;
import com.zerobear.UserService.exception.UserNotFoundException;
import com.zerobear.UserService.exception.ValidationException;
import com.zerobear.UserService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try{
            UserResponse userResponse= userService.getUser(id);
            return  new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    // skip it for future not decided how  new user will be created without register
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegisterRequest userRegisterRequest){
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest){
        try {
            UserResponse userResponse = userService.updateUser(updateUserRequest);
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        }catch (ValidationException e){
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            return  new ResponseEntity<>("Some Error-Occur ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
