package com.bank.application.backend.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Submission extends ItemClass {

    private LocalDate submissionDate;
    private Boolean isApproved;

    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private Credit credit;

    public Submission() {
        this.submissionDate = LocalDate.now();
        this.isApproved = false;
    }

    public Submission(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
        this.isApproved = false;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }
}
