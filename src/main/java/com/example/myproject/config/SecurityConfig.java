package com.example.myproject.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> permittedEndpoints = List.of(
            "/api/users/register",
            "/api/users/login",
            "/api/users/**",
            "api/rooms",
            "api/rooms/**"
        );
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(permittedEndpoints.toArray(String[]::new)).permitAll()
                .anyRequest().authenticated() // Other endpoints require authentication
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Access denied\", \"message\": \"You do not have permission to access this resource.\"}");
                })
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
