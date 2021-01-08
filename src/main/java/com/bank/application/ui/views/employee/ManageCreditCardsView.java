package com.bank.application.ui.views.employee;

import com.bank.application.backend.entity.CreditCard;
import com.bank.application.backend.service.CreditCardService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.backend.service.TransactionService;
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

@Route(value = "manageCreditCards", layout = MainView.class)
@PageTitle("ManageCreditCards | BankAP")
public class ManageCreditCardsView extends VerticalLayout {
    private final CreditCardService creditCardService;
    private final SubmissionService submissionService;
    private final TransactionService transactionService;

    public ManageCreditCardsView(CreditCardService creditCardService, SubmissionService submissionService, TransactionService transactionService) {
        this.creditCardService = creditCardService;
        this.submissionService = submissionService;
        this.transactionService = transactionService;

        createTabs();
    }

    private void createTabs() {
        Tab unapprovedTab = new Tab("Unapproved");
        Tab approvedTab = new Tab("Approved");

        GridCrud<CreditCard> unapprovedGrid = createGrid(false);
        GridCrud<CreditCard> approvedGrid = createGrid(true);

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

    private GridCrud<CreditCard> createGrid(boolean isApproved) {
        GridCrud<CreditCard> crud = new GridCrud<>(CreditCard.class);

        crud.getGrid().setColumns("account", "creditCardNumber", "limitAmount", "startDate", "submissionApproved");
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE, "submissionApproved");
        crud.getCrudFormFactory().setFieldCaptions(CrudOperation.UPDATE, "Approve?");

        crud.setFindAllOperation(() -> creditCardService.findAllBySubmissionApproved(isApproved));

        if (isApproved) {
            crud.setUpdateOperationVisible(false);
        } else {
            crud.setUpdateOperation(creditCard -> {
                boolean approvedByEmployee = creditCard.getSubmissionApproved();

                if (approvedByEmployee) {
                    submissionService.update(creditCard.getSubmission());
                    Notification.show("Submission approved!");
                } else {
                    Notification.show("Submission not approved!");
                    return null;
                }

                return creditCard;
            });
        }

        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));
        crud.setShowNotifications(false);
        crud.setAddOperationVisible(false);
        crud.setDeleteOperationVisible(false);
        setSizeFull();
        return crud;
    }
}
