package com.zerobear.AuthService.config;

import com.zerobear.AuthService.entity.Role;
import com.zerobear.AuthService.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Bean
    public CommandLineRunner initializeData() {
        return args -> {

            if (!roleRepository.existsByRole("USER")) {
                Role user = new Role();
                user.setRole("ADMIN");
                roleRepository.save(user);
            }

            if (!roleRepository.existsByRole("ADMIN")) {
                Role admin = new Role();
                admin.setRole("ADMIN");
                roleRepository.save(admin);
            }
        };
    }
}