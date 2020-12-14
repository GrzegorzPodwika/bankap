package com.bank.application.backend.entity;

import javax.persistence.Entity;

@Entity
public class Submission extends Person {

    private String date;

    public Submission(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
