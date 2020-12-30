package com.bank.application.ui.views.employee;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.TransactionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.home.HomeView;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Optional;

import static com.bank.application.ui.views.home.HomeView.*;

@Route(value = "managePayments", layout = MainView.class)
@PageTitle("ManagePayments")
public class ManagePaymentsEmployeeView extends VerticalLayout {
    private final AccountService accountService;
    private final UserService userService;

    public ManagePaymentsEmployeeView(TransactionService transactionService, AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;

        GridCrud<Transaction> crud = new GridCrud<>(Transaction.class);

        crud.getGrid().setColumns("account", "transactionTitle", "amount", "date", "receiverAccountNumber");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);

        crud.setAddOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.setFindAllOperation(transactionService::findAllByTimestamp);

        crud.setDeleteOperation(transaction -> {
            if (!transaction.getAccount().getAccountNumber().equals(transaction.getReceiverAccountNumber())) {
                Optional<Account> searchReceiver = accountService.existAccountByAccountNumber(transaction.getReceiverAccountNumber());

                if (searchReceiver.isPresent()) {
                    Account receiverAccount = searchReceiver.get();

                    Transaction searchReceiverTransaction = transactionService.findByAccountAndTitle(receiverAccount, transaction.getTransactionTitle());

                    if (searchReceiverTransaction != null) {
                        updateBalanceInSenderAndReceiverAccounts(transaction.getAccount(), receiverAccount, transaction.getAmount());
                        transactionService.delete(searchReceiverTransaction);
                        transactionService.delete(transaction);
                        Notification.show("Transakcja anulowana pomyślnie");
                    } else {
                        Notification.show("Ups. We don't find receiver transaction");
                    }

                } else {
                    Notification.show("Ups. We don't find receiver account");
                }
            } else {
                updateBalanceInSenderAccount(transaction);
                transactionService.delete(transaction);
                Notification.show("Transakcja anulowana pomyślnie");
            }

        });

        setSizeFull();
        add(crud);
    }

    private void updateBalanceInSenderAndReceiverAccounts(Account senderAccount, Account receiverAccount, double transferAmount) {
        transferAmount = Math.abs(transferAmount);

        double newBalanceSender = Double.parseDouble(senderAccount.getAccountBalance()) + transferAmount;
        newBalanceSender = Math.round(newBalanceSender * 100) / 100.0;
        senderAccount.setAccountBalance(Double.toString(newBalanceSender));

        double newBalanceReceiver = Double.parseDouble(receiverAccount.getAccountBalance()) - transferAmount;
        newBalanceReceiver = Math.round(newBalanceReceiver * 100) / 100.0;
        receiverAccount.setAccountBalance(Double.toString(newBalanceReceiver));

        accountService.update(senderAccount);
        accountService.update(receiverAccount);
    }

    private void updateBalanceInSenderAccount(Transaction transaction) {
        Account senderAccount = transaction.getAccount();
        double newBalanceSender = Double.parseDouble(senderAccount.getAccountBalance()) + (transaction.getAmount() * -1.0);
        newBalanceSender = Math.round(newBalanceSender * 100) / 100.0;
        senderAccount.setAccountBalance(Double.toString(newBalanceSender));

        accountService.update(senderAccount);
    }
}
