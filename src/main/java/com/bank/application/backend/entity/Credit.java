package com.bank.application.backend.entity;

import javax.persistence.*;

@Entity
public class Credit extends ItemClass {

    private String begin;
    private String end;
    private int amount;
    private int numberOfInstallments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Transient
    private String submissionDate;

    public String getSubmissionDate() {
        return submission.getDate();
    }

    public void setSubmissionDate(String submissionDate) {
        submission.setDate(submissionDate);
    }

    @Transient
    private Boolean submissionApproved;

    public Boolean getSubmissionApproved() {
        return submission.getApproved();
    }

    public void setSubmissionApproved(Boolean submissionApproved) {
        submission.setApproved(submissionApproved);
    }

    public Credit() {

    }

    public Credit(String begin, String end, int amount, int numberOfInstallments) {
        this.begin = begin;
        this.end = end;
        this.amount = amount;
        this.numberOfInstallments = numberOfInstallments;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
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
