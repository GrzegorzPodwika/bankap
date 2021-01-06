package com.bank.application.ui.views.employee;

import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.CreditService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

@Route(value = "submission", layout = MainView.class)
@PageTitle("Submission | BankAP")
public class SubmissionView extends VerticalLayout {
    private final CreditService creditService;
    private final SubmissionService submissionService;

    public SubmissionView(CreditService creditService, SubmissionService submissionService) {
        this.creditService = creditService;
        this.submissionService = submissionService;

        createGrid();
    }

    public void createGrid() {
        GridCrud<Credit> crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("submissionDate", "amount", "numberOfInstallments", "begin", "end", "submissionApproved");

        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE,  "submissionApproved");

        crud.getCrudFormFactory().setFieldCaptions(CrudOperation.UPDATE, "Approve");

        crud.setFindAllOperation(() -> creditService.findAllBySubmissionApproved(false));
        crud.setUpdateOperation(credit -> {
            Submission submission = submissionService.findById(credit.getSubmission().getId()).get();

            submission.setApproved(credit.getSubmission().getApproved());
            submissionService.save(submission);
            Notification.show("Submission approved");

            return credit;
        });

        crud.setShowNotifications(false);

        crud.setAddOperationVisible(false);
        crud.setDeleteOperationVisible(false);

        setSizeFull();
        add(crud);
    }
}
