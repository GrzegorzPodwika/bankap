package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT t FROM Transaction t WHERE account = ?1")
    List<Transaction> findAllTransactions(Account account);

    @Query(value = "SELECT t FROM Transaction t WHERE account = ?1 AND date >= ?2")
    List<Transaction> findAllTransactionsByDate(Account account, String startDate);

    List<Transaction> findAllByOrderByTimestampDesc();

    Transaction findByAccountAndTransactionTitle(Account account, String transactionTitle);
}
