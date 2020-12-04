package com.example.application.data.service;

import com.example.application.data.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getByUsername(String username);
}
