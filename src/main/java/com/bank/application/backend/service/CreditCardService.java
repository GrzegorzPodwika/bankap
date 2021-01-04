package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.CreditCard;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCardRepository getCreditCardRepository() {
        return creditCardRepository;
    }

    public List<CreditCard> findAllByAccount(Account account) {
        return creditCardRepository.findAllCreditCards(account);
    }

    public void delete(CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    public void save(CreditCard creditCard) { creditCardRepository.save(creditCard); }
}
