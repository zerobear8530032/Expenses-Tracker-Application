package com.zerobear.AuthService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import tools.jackson.databind.ObjectMapper;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return  new ObjectMapper();
    }
}
