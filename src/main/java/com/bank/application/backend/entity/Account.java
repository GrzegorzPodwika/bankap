package com.bank.application.backend.entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
public class Account extends ItemClass {

    private String accountNumber;
    private String accountBalance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Credit> creditList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<CreditCard> creditCardList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Account(String accountNumber) {
        this.accountBalance = "0";
        this.accountNumber = accountNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
