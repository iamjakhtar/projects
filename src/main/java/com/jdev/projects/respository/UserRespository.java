package com.jdev.projects.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.jdev.projects.model.User;

@Repository
public interface UserRespository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
