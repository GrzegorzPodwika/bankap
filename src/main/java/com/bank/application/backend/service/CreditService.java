package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditService implements Dao<Credit>{

    @Autowired
    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public Credit save(Credit credit) {
        return creditRepository.save(credit);
    }

    @Override
    public Credit update(Credit credit) {
        return creditRepository.save(credit);
    }

    @Override
    public void delete(Credit credit) {
        creditRepository.delete(credit);
    }

    @Override
    public Optional<Credit> get(Integer id) {
        return creditRepository.findById(id);
    }

    @Override
    public List<Credit> getAll() {
        return creditRepository.findAll();
    }

    public List<Credit> findAllBySubmissionApproved(Boolean submissionApproved) {
        return creditRepository.findAllBySubmissionApproved(submissionApproved);
    }

    public List<Credit> findAllByAccount(Account account) {
        return creditRepository.findAllCredits(account);
    }
}
