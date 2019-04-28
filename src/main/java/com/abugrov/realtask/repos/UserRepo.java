package com.abugrov.realtask.repos;

import com.abugrov.realtask.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    User findByEmail(String email);
}