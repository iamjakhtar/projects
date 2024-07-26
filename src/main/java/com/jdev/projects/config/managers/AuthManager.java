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
        // here we are using authentication.getName() and not getUsername()
        User user = userService.getUserByUsername(authentication.getName());
        //here getCredentials() returns an object so we have to do toString() method match with the password string.
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        // if password matches then we create a UsernamePasswordAuthenticationToken and return it the token.
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
    
}
