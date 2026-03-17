package com.example.studentapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Tells Spring this class contains setup instructions
public class SecurityConfig {

    @Bean // Tells Spring to create this object and keep it ready to inject anywhere!
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}