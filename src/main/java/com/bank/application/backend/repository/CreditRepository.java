package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Credit;
import org.springframework.data.repository.CrudRepository;

public interface CreditRepository extends CrudRepository<Credit, Integer> {
}
