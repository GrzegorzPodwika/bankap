package com.bank.application.ui.views.user.credit;

import com.bank.application.backend.entity.*;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.CreditService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.home.HomeView.UserNotFoundException;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.time.LocalDate;
import java.util.*;

import static com.bank.application.other.Constants.MAX_NUMBER_OF_CREDITS;

@Route(value = "credits", layout = MainView.class)
@PageTitle("Credits | BankAP")
@CssImport("./styles/views/credits/credits-view.css")
public class CreditView extends Div {

    private User activeUser;
    private final CreditService creditService;
    private final UserService userService;
    private final SubmissionService submissionService;
    private final AccountService accountService;
    private GridCrud<Credit> crud;


    public CreditView(CreditService creditService, UserService userService, SubmissionService submissionService, AccountService accountService) {
        setClassName("credit-view");

        this.submissionService = submissionService;
        this.userService = userService;
        this.creditService = creditService;
        this.accountService = accountService;
        fetchActiveUser();

        createGrid();
    }

    private void fetchActiveUser() {
        try {
            fetchUserById();
        } catch (UserNotFoundException e) {
            System.out.println("User has not been found!");
        }
    }

    private void fetchUserById() throws UserNotFoundException {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute(Constants.USER_ID);
        Optional<User> fetchedUpdatedUser = userService.get(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public void createGrid() {
        crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("amount", "numberOfInstallments", "monthlyInstallment", "remainingInstallments",
                "beginDate", "endDate", "submissionApproved");

        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,
                "amount", "numberOfInstallments", "beginDate");
        crud.getGrid().setSelectionMode(Grid.SelectionMode.SINGLE);

        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));

        crud.setFindAllOperation(() -> creditService.findAllByAccount(activeUser.getAccount()));
        checkIfNumberOfCreditsIsMax();

        crud.setAddOperation(creditForm -> {

            if (creditForm.getNumberOfInstallments() == 0) {
                Notification.show("Liczba rat nie może być równa zero!");
                return null;
            } else if(creditForm.getAmount() == 0) {
                Notification.show("Wartość kredytu nie może być równa zero!");
                return null;
            } else if(creditForm.getBeginDate() == null) {
                Notification.show("Wybierz datę początku kredytu!");
                return null;
            } else {
                Account account = activeUser.getAccount();
                Submission submission = new Submission();

                Credit credit = new Credit();
                credit.setAmount(creditForm.getAmount());
                credit.setNumberOfInstallments(creditForm.getNumberOfInstallments());
                credit.setMonthlyInstallment(calculateNumberOfInstallment(creditForm));
                credit.setRemainingInstallments(creditForm.getNumberOfInstallments());
                credit.setBeginDate(creditForm.getBeginDate());
                credit.setEndDate(calculateEndDate(creditForm));
                credit.setAccount(account);
                credit.setSubmission(submission);

                //submissionService.save(submission);
                creditService.save(credit);
                //incrementNumberOfCreditsInClientAccount();
                checkIfNumberOfCreditsIsMax();

                Notification.show("Pomyślnie utworzono kredyt!", 3000, Notification.Position.MIDDLE);
                return credit;
            }
        });

        crud.setShowNotifications(false);

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.setDeleteOperationVisible(false);
/*        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.DELETE, "amount", "numberOfInstallments",
                "begin", "end");
        crud.setDeleteOperation(creditService::delete);*/

        setSizeFull();
        add(crud);
    }

    private double calculateNumberOfInstallment(Credit creditForm) {
        return 1.05 * creditForm.getAmount()/ creditForm.getNumberOfInstallments();
    }

    private LocalDate calculateEndDate(Credit creditForm) {
        return creditForm.getBeginDate().plusMonths(creditForm.getNumberOfInstallments());
    }

/*    private void incrementNumberOfCreditsInClientAccount() {
        Account account = activeUser.getAccount();
        account.incrementNumberOfCredits();
        accountService.update(account);
    }*/

    private void checkIfNumberOfCreditsIsMax() {
        if (activeUser.getAccount().getNumberOfCredits() == MAX_NUMBER_OF_CREDITS)
            crud.setAddOperationVisible(false);
    }

}
