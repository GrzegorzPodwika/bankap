package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionService implements Dao<Transaction>{
    private final TransactionRepository repo;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.repo = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return repo.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        return repo.save(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        repo.delete(transaction);
    }

    @Override
    public Optional<Transaction> get(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Transaction> getAll() {
        return repo.findAll();
    }

    public List<Transaction> findAllByAccount(Account account) {
        return repo.findAllTransactions(account);
    }

    public List<Transaction> findAllByAccountAndDate(Account account, String startDate) {
        return repo.findAllTransactionsByDate(account, startDate);
    }

    public List<Transaction> findAllByTimestamp() {
        return repo.findAllByOrderByTimestampDesc();
    }

    public Transaction findByAccountAndTitle(Account receiverAccount, String transactionTitle) {
        return repo.findByAccountAndTransactionTitle(receiverAccount, transactionTitle);
    }
}
