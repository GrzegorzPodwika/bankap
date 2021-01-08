package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.CreditCard;
import com.bank.application.backend.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements Dao<CreditCard>{
    @Autowired
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard update(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public void delete(CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    @Override
    public Optional<CreditCard> get(Integer id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public List<CreditCard> getAll() {
        return creditCardRepository.findAll();
    }

    public List<CreditCard> findAllByAccount(Account account) {
        return creditCardRepository.findAllCreditCards(account);
    }

    public List<CreditCard> findAllBySubmissionApproved(boolean submissionApproved) {
        return creditCardRepository.findAllBySubmissionApproved(submissionApproved);
    }
}
