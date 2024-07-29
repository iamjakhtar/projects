package com.jdev.projects.config.managers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jdev.projects.model.User;
import com.jdev.projects.service.UserService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthManager implements AuthenticationManager {
    
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = this.userService.getUserByUsername(authentication.getPrincipal().toString());

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided the wrong password.");
            
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());


    }   
}