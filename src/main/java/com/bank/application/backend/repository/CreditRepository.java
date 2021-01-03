package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {
    @Query(value = "SELECT c FROM Credit c WHERE account = ?1")
    List<Credit> findAllCredits(Account account);
}
