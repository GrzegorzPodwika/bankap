package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.repository.AccountRepository;
import com.bank.application.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService{
    private final TransactionRepository repo;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.repo = transactionRepository;
    }

    public TransactionRepository getRepo() {
        return repo;
    }


    public List<Transaction> findAll() {
        return repo.findAll();
    }


    public void add(Transaction transactionFromSender, Transaction transactionForReceiver) {
        repo.save(transactionFromSender);
        repo.save(transactionForReceiver);
    }


    public Transaction update(Transaction transaction) {
        return repo.save(transaction);
    }


    public void delete(Transaction transaction) {
        repo.delete(transaction);
    }

    public List<Transaction> findAllByAccount(Account account) {
        return repo.findAllTransactions(account);
    }

    public void save(Transaction transaction) {
        repo.save(transaction);
    }
}
