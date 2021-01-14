package com.bank.application.ui.views.employee;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.TransactionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Optional;

@Route(value = "managePayments", layout = MainView.class)
@PageTitle("ManagePayments")
public class ManagePaymentsEmployeeView extends VerticalLayout {
    private final AccountService accountService;

    public ManagePaymentsEmployeeView(TransactionService transactionService, AccountService accountService) {
        this.accountService = accountService;

        GridCrud<Transaction> crud = new GridCrud<>(Transaction.class);

        crud.getGrid().setColumns("account", "transactionTitle", "amount", "date", "receiverAccountNumber");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);

        crud.setAddOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.setFindAllOperation(transactionService::findAllByTimestamp);

        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));

        crud.setDeleteOperation(transaction -> {
            if (!transaction.getAccount().getAccountNumber().equals(transaction.getReceiverAccountNumber())) {
                Optional<Account> searchReceiver = accountService.existAccountByAccountNumber(transaction.getReceiverAccountNumber());

                if (searchReceiver.isPresent()) {
                    Account receiverAccount = searchReceiver.get();

                    Transaction searchReceiverTransaction = transactionService.findByAccountAndTitle(receiverAccount, transaction.getTransactionTitle());

                    if (searchReceiverTransaction != null) {
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
                transactionService.delete(transaction);
                Notification.show("Transakcja anulowana pomyślnie");
            }

        });

        setSizeFull();
        add(crud);
    }

}
