package com.bank.application.backend.entity;

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

    public List<Credit> getCreditList() {
        return creditList;
    }

    public void setCreditList(List<Credit> creditList) {
        this.creditList = creditList;
    }

    public List<CreditCard> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }
}
