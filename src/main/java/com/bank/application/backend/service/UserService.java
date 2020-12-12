package com.bank.application.backend.service;

import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserRepository getRepository() {
        return userRepository;
    }
}
