package com.bank.application.backend.service;

import com.bank.application.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
