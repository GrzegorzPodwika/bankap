package com.bank.application.backend.repository;

import com.bank.application.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getByUsername(String username);
}
