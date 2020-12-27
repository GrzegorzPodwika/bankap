package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Submission;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepository extends CrudRepository<Submission, Integer> {
}
