package com.bank.application.backend.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class Account extends ItemClass {

    private String accountNumber;
    private double accountBalance;
    private int numberOfCreditCards;
    private int numberOfCredits;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {
        this.numberOfCredits = 0;
        this.numberOfCreditCards = 0;
    }

    public Account(String accountNumber) {
        this.accountBalance = 0.0;
        this.accountNumber = accountNumber;
        this.numberOfCredits = 0;
        this.numberOfCreditCards = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getNumberOfCreditCards() {
        return numberOfCreditCards;
    }

    public void incrementNumberOfCreditCards() {
        this.numberOfCreditCards += 1;
    }

    public void setNumberOfCreditCards(int numberOfCreditCards) {
        this.numberOfCreditCards = numberOfCreditCards;
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

    public void incrementNumberOfCredits() {
        this.numberOfCredits += 1;
    }

    public void setNumberOfCredits(int numberOfCredits) {
        this.numberOfCredits = numberOfCredits;
    }

    @Override
    public String toString() {
        return accountNumber;
    }
}
