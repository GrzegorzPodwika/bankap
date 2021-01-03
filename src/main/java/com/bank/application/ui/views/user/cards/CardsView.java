package com.bank.application.ui.views.user.cards;

import com.bank.application.backend.entity.*;
import com.bank.application.backend.service.CreditCardService;
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

@Route(value = "cards", layout = MainView.class)
@PageTitle("Cards")
@CssImport("./styles/views/cards/cards-view.css")
public class CardsView extends Div {

    private User activeUser;
    private final CreditCardService creditCardService;
    private final UserService userService;


    public CardsView(CreditCardService creditCardService, UserService userService) {
        setId("cards-view");
        this.userService = userService;
        this.creditCardService = creditCardService;

        fetchActiveUser();

        GridCrud<CreditCard> crud = new GridCrud<>(CreditCard.class);

        crud.getGrid().setColumns("creditCardNumber");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD);

        crud.setFindAllOperation(() -> creditCardService.findAllByAccount(activeUser.getAccount()));

        crud.setAddOperation(transaction -> {
            CreditCard creditCard = new CreditCard();
            Account account = activeUser.getAccount();

            creditCard.setAccount(account);
            creditCard.setCreditCardNumber(generateRandomCardNumber());

            creditCardService.save(creditCard);

            Notification.show("Pomyślnie utworzono kartę kredytową!", 3000, Notification.Position.MIDDLE);
            return creditCard;
        });

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.DELETE, "creditCardNumber");
        crud.setDeleteOperation(creditCardService::delete);

        setSizeFull();
        add(crud);
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
        Optional<User> fetchedUpdatedUser = userService.findUserById(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public String generateRandomCardNumber() {
        int min = 0;
        int max = 9;
        String cardNumber = "";

        for (int i = 0; i < 16; i++) {
            int randomNum = (int)(Math.random() * (max - min + 1) + min);
            cardNumber += Integer.toString(randomNum);
        }

        return cardNumber;
    }

    public static class UserNotFoundException extends Exception {

    }
}
