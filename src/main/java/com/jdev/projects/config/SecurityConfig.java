package com.jdev.projects.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.jdev.projects.config.filters.AuthFilter;
import com.jdev.projects.config.filters.ExceptionFilter;
import com.jdev.projects.config.filters.JwtAuthFilter;
import com.jdev.projects.config.managers.AuthManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    
    private AuthManager authManager;
    @Bean
    SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {
        AuthFilter authFilter = new AuthFilter(authManager);
        authFilter.setFilterProcessesUrl("/auth");
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET).permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new ExceptionFilter(), AuthFilter.class)
            .addFilter(authFilter)
            .addFilterAfter(new JwtAuthFilter(), AuthFilter.class)
            .sessionManagement(policy -> policy.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}