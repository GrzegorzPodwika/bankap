package com.bank.application.backend.service;

import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionService {
    @Autowired
    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public SubmissionRepository getSubmissionRepository() {
        return submissionRepository;
    }

    public Optional<Submission> findById(Integer id) {

        return submissionRepository.findById(id);
    }

    public void save(Submission submission) {
        submissionRepository.save(submission);
    }
}
