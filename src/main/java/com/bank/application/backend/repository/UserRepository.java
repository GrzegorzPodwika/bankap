package com.bank.application.backend.repository;

import com.bank.application.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getByUsername(String username);

    @Query("SELECT u from User u WHERE role != 2")
    List<User> findAllClientsAndEmployees();
}
