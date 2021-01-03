package com.bank.application.ui.views.user.credit;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Credit;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.CreditService;
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


    public CreditView(CreditService creditService, UserService userService) {
        setId("credit-view");
        this.userService = userService;
        this.creditService = creditService;

        fetchActiveUser();

        GridCrud<Credit> crud = new GridCrud<>(Credit.class);

        crud.getGrid().setColumns("amount", "numberOfInstallments", "begin", "end");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, "amount", "numberOfInstallments",
                "begin", "end");

        crud.setFindAllOperation(() -> creditService.findAllByAccount(activeUser.getAccount()));

        crud.setAddOperation(creditForm -> {
            Credit credit = new Credit();
            Account account = activeUser.getAccount();

            credit.setAmount(creditForm.getAmount());
            credit.setNumberOfInstallments(creditForm.getNumberOfInstallments());
            credit.setBegin(creditForm.getBegin());
            credit.setEnd(creditForm.getEnd());
            credit.setAccount(account);

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
