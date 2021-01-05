package com.bank.application.backend.entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class User extends Person {

    private String username;
    private String passwordSalt;
    private String passwordHash;
    private Role role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id",  referencedColumnName = "id")
    private Account account;

    public User() {
    }

    public User(String username, String plainPassword, Role role, String firstName, String lastName,
                String pesel, String address, String email, String phone, String birthDate) {
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
        this.setBirthDate(birthDate);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", passwordSalt='" + passwordSalt + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                ", account=" + account +
                '}';
    }
}
