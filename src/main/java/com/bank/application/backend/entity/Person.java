package com.bank.application.backend.entity;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
public class Person extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int pesel;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public int getPesel() { return pesel; }
    public void setPesel(int pesel) { this.pesel = pesel; }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
