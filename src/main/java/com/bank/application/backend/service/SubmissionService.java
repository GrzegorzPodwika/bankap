package com.bank.application.backend.service;

import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService implements Dao<Submission> {
    @Autowired
    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Submission save(Submission submission) {
        return submissionRepository.save(submission);
    }

    @Override
    public Submission update(Submission submission) {
        return submissionRepository.save(submission);
    }

    @Override
    public void delete(Submission submission) {
        submissionRepository.delete(submission);
    }

    @Override
    public Optional<Submission> get(Integer id) {
        return submissionRepository.findById(id);
    }

    @Override
    public List<Submission> getAll() {
        return submissionRepository.findAll();
    }

}
