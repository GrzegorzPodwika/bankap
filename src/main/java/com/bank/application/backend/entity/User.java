package com.bank.application.backend.entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends Person {

    private String username;
    private String passwordSalt;
    private String passwordHash;
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<CreditCard> creditCardList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Transaction> transactionList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Submission> submissionList;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public User() {
    }

    public User(String username, String plainPassword, Role role, String firstName, String lastName,
                String pesel, String address, String email, String phone) {
        this.username = username;
        this.role = role;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(plainPassword + passwordSalt);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPesel(pesel);
        this.setAddress(address);
        this.setEmail(email);
        this.setPhone(phone);

        account = new Account(generateRandomAccountNumber());
    }

    public String generateRandomAccountNumber() {
        int min = 0;
        int max = 9;
        String accountNumber = "";

        for (int i = 0; i < 26; i++) {
            int randomNum = (int)(Math.random() * (max - min + 1) + min);
            accountNumber += Integer.toString(randomNum);
        }

        return accountNumber;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<CreditCard> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<Submission> getSubmissionList() {
        return submissionList;
    }

    public void setSubmissionList(List<Submission> submissionList) {
        this.submissionList = submissionList;
    }

    public boolean checkPassword(String plainPassword) {
        return DigestUtils.sha1Hex(plainPassword + passwordSalt).equals(passwordHash);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
