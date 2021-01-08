package com.bank.application.backend.repository;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.CreditCard;
import com.bank.application.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    @Query(value = "SELECT c FROM CreditCard c WHERE account = ?1")
    List<CreditCard> findAllCreditCards(Account account);

    @Query(value = "SELECT c from CreditCard c WHERE c.submission.isApproved = ?1")
    List<CreditCard> findAllBySubmissionApproved(boolean submissionApproved);
}
