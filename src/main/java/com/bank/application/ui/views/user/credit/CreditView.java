package com.bank.application.ui.views.user.credit;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.Submission;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.CreditService;
import com.bank.application.backend.service.SubmissionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Optional;

@Route(value = "credits", layout = MainView.class)
@PageTitle("Credits | BankAP")
@CssImport("./styles/views/credits/credits-view.css")
public class CreditView extends Div {

    private User activeUser;
    private final CreditService creditService;
    private final UserService userService;
    private final SubmissionService submissionService;


    public CreditView(CreditService creditService, UserService userService, SubmissionService submissionService) {
        this.submissionService = submissionService;
        this.userService = userService;
        this.creditService = creditService;

        setId("credit-view");

        fetchActiveUser();

        createGrid();
    }


    public void createGrid() {
        GridCrud<Credit> crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("amount", "numberOfInstallments", "begin", "end", "submissionApproved");

        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, "amount", "numberOfInstallments",
                "begin", "end");

        crud.setFindAllOperation(() -> creditService.findAllByAccount(activeUser.getAccount()));

        crud.setAddOperation(creditForm -> {
            Credit credit = new Credit();
            Account account = activeUser.getAccount();
            Submission submission = new Submission();
            submission.setAccount(account);

            credit.setAmount(creditForm.getAmount());
            credit.setNumberOfInstallments(creditForm.getNumberOfInstallments());
            credit.setBegin(creditForm.getBegin());
            credit.setEnd(creditForm.getEnd());
            credit.setAccount(account);
            credit.setSubmission(submission);

            submissionService.save(submission);
            creditService.save(credit);

            Notification.show("Pomyślnie utworzono kredyt!", 3000, Notification.Position.MIDDLE);
            return credit;
        });

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.DELETE, "amount", "numberOfInstallments",
                "begin", "end");
        crud.setDeleteOperation(creditService::delete);

        setSizeFull();
        add(crud);
    }

    private void fetchActiveUser() {
        try {
            fetchUserById();
        } catch (com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException e) {
            System.out.println("User has not been found!");
        }
    }

    private void fetchUserById() throws com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute(Constants.USER_ID);
        Optional<User> fetchedUpdatedUser = userService.findUserById(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new com.bank.application.ui.views.user.cards.CardsView.UserNotFoundException();
        }
    }

    public static class UserNotFoundException extends Exception {

    }
}
