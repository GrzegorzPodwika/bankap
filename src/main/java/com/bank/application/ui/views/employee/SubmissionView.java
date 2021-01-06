package com.bank.application.ui.views.employee;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.CreditService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.HashMap;
import java.util.Map;

@Route(value = "submission", layout = MainView.class)
@PageTitle("Submission | BankAP")
public class SubmissionView extends VerticalLayout {
    private final CreditService creditService;
    private final SubmissionService submissionService;
    private final AccountService accountService;

    public SubmissionView(CreditService creditService, SubmissionService submissionService, AccountService accountService) {
        this.creditService = creditService;
        this.submissionService = submissionService;
        this.accountService = accountService;

        createTabs();
    }

    public GridCrud<Credit> createGrid(Boolean approved) {
        GridCrud<Credit> crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("submissionDate", "accountNumber", "amount", "numberOfInstallments",
                "begin", "end", "submissionApproved");

        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE,  "submissionApproved");

        crud.getCrudFormFactory().setFieldCaptions(CrudOperation.UPDATE, "Approve");

        crud.setFindAllOperation(() -> creditService.findAllBySubmissionApproved(approved));

        if (approved) {
            crud.setUpdateOperationVisible(false);
        }
        else {
            crud.setUpdateOperation(credit -> {
                Submission submission = submissionService.findById(credit.getSubmission().getId()).get();
                Account account = accountService.findById(credit.getAccount().getId()).get();

                submission.setApproved(credit.getSubmission().getApproved());
                Long newAccountBalance = Long.parseLong(account.getAccountBalance()) + credit.getAmount();

                account.setAccountBalance(Long.toString(newAccountBalance));
                accountService.save(account);

                submissionService.save(submission);
                Notification.show("Submission approved");

                return credit;
            });
        }


        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));

        crud.setShowNotifications(false);

        crud.setAddOperationVisible(false);
        crud.setDeleteOperationVisible(false);

        setSizeFull();
        return crud;
    }

    public void createTabs() {
        Tab unapprovedTab = new Tab("Unapproved");
        Tab approvedTab = new Tab("Approved");

        GridCrud<Credit> unapprovedGrid = createGrid(false);
        GridCrud<Credit> approvedGrid = createGrid(true);
        approvedGrid.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(unapprovedTab, unapprovedGrid);
        tabsToPages.put(approvedTab, approvedGrid);
        Tabs tabs = new Tabs(unapprovedTab, approvedTab);
        Div pages = new Div(unapprovedGrid, approvedGrid);
        pages.setSizeFull();

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }
}
