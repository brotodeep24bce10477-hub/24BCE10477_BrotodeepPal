package com.example.bookmanagement.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bookmanagement.model.User;

public interface UserRepository
        extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}