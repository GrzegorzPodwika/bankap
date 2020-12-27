package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
}
