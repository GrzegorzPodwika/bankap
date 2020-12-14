package com.bank.application.backend.entity;

import javax.persistence.Entity;

@Entity
public class Credit extends Person {

    private String begin;
    private String end;
    private int amount;
    private int numberOfInstallments;

    public Credit(String begin, String end, int amount, int numberOfInstallments) {
        this.begin = begin;
        this.end = end;
        this.amount = amount;
        this.numberOfInstallments = numberOfInstallments;
    }

    public String getBegin() {
        return begin;
    }
    public void setBegin(String begin) {
        this.begin = begin;
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }
    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
}
