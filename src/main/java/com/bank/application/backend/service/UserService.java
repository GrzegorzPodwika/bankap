package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.repository.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getRepository() {
        return userRepository;
    }

    public String getCurrentUserName() {
        return VaadinSession.getCurrent().getAttribute(User.class).getUsername();
    }

    public String getCurrentFirstName() {
        return VaadinSession.getCurrent().getAttribute(User.class).getFirstName();
    }

    public String getCurrentLastName() {
        return VaadinSession.getCurrent().getAttribute(User.class).getLastName();
    }

    public String getCurrentPesel() {
        return VaadinSession.getCurrent().getAttribute(User.class).getPesel();
    }

    public String getCurrentAddress() {
        return VaadinSession.getCurrent().getAttribute(User.class).getAddress();
    }

    public String getCurrentEmail() {
        return VaadinSession.getCurrent().getAttribute(User.class).getEmail();
    }

    public String getCurrentPhone() {
        return VaadinSession.getCurrent().getAttribute(User.class).getPhone();
    }

    public List<Transaction> getTransactionList() {
        return VaadinSession.getCurrent().getAttribute(User.class).getTransactionList();
    }

    public String getAccountNumber() {
        return VaadinSession.getCurrent().getAttribute(User.class).getAccount().getAccountNumber();
    }

    public String getAccountBalance() {
        return VaadinSession.getCurrent().getAttribute(User.class).getAccount().getAccountBalance();
    }
}
