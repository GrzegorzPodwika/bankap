package com.bank.application.backend.repository;

import com.bank.application.backend.entity.CreditCard;
import org.springframework.data.repository.CrudRepository;

public interface CreditCardRepository extends CrudRepository<CreditCard, Integer> {
}
