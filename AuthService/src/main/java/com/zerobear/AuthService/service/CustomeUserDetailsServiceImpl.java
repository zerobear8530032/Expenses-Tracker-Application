package com.zerobear.AuthService.service;

import com.zerobear.AuthService.entity.User;
import com.zerobear.AuthService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomeUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByName(username).orElseThrow(()-> new UsernameNotFoundException("User name : "+username+" not found Exception"));
        CustomUserDetails userDetails= new CustomUserDetails(user.getId(),user.getName(),user.getPassword(),user.getRoles());
        return userDetails;
    }
}
