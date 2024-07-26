package com.jdev.projects.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jdev.projects.model.User;
import com.jdev.projects.respository.UserRespository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    
    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRespository.save(user);
    }

    public User getUserByUsername(String username) {
        Optional<User> user = this.userRespository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(username);
        }
    }

    
}
