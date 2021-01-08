package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements Dao<Account> {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public Optional<Account> get(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> existAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }


}

