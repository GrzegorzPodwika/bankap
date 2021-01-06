package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {
    @Autowired
    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public CreditRepository getCreditRepository() {
        return creditRepository;
    }

    public List<Credit> findAllByAccount(Account account) {
        return creditRepository.findAllCredits(account);
    }

    public List<Credit> findAllCredits() {
        return creditRepository.findAll();
    }

    public void delete(Credit credit) {
        creditRepository.delete(credit);
    }

    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    public List<Credit> findAllBySubmissionApproved(Boolean submissionApproved) {
        return creditRepository.findAllBySubmissionApproved(submissionApproved);
    }
}
