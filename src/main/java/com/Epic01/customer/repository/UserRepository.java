package com.Epic01.customer.repository;

import com.Epic01.customer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer>{
    Optional<User> findByEmail(String email);
}
