package com.jdev.projects.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                AuthFilter authFilter = new AuthFilter(authManager);
                authFilter.setFilterProcessesUrl("/authenticate");
                http
                        .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                        .requestMatchers(HttpMethod.GET).permitAll()
                                        .anyRequest().authenticated())
                        .csrf(csrf -> csrf.disable()) // without this line post and delete request didn't work
                        .addFilterBefore(new ExceptionFilter(), AuthFilter.class)
                        .addFilter(authFilter)
                        .addFilterAfter(new JwtAuthFilter(), AuthFilter.class)
                        .sessionManagement(policy -> policy
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();

        }
  
}
