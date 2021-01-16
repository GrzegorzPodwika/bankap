package com.bank.application.ui.views.user.cards;

import com.bank.application.backend.entity.*;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.CreditCardService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.home.HomeView.UserNotFoundException;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import com.vaadin.flow.server.VaadinSession;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.time.LocalDate;
import java.util.Optional;

import static com.bank.application.other.Constants.MAX_NUMBER_OF_CREDIT_CARDS;

@Route(value = "cards", layout = MainView.class)
@PageTitle("Cards")
@CssImport("./styles/views/cards/cards-view.css")
public class CardsView extends Div {

    private User activeUser;
    private final CreditCardService creditCardService;
    private final AccountService accountService;
    private final UserService userService;
    private final GridCrud<CreditCard> crud;


    public CardsView(CreditCardService creditCardService, AccountService accountService, UserService userService) {
        setId("cards-view");
        this.userService = userService;
        this.creditCardService = creditCardService;
        this.accountService = accountService;
        fetchActiveUser();

        crud = new GridCrud<>(CreditCard.class);
        crud.getGrid().setSelectionMode(Grid.SelectionMode.SINGLE);

        crud.getGrid().setColumns("creditCardNumber", "limitAmount", "usedFunds", "submissionApproved", "blocked");
        crud.getGrid().setColumnReorderingAllowed(true);
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,
                "limitAmount", "startDate");

        crud.setFindAllOperation(() -> creditCardService.findAllByAccount(activeUser.getAccount()));
        checkIfNumberOfCreditCardsIsMax();

        Button buttonBlockCard = new Button("Block/Unblock card");
        buttonBlockCard.setId("button-block-card");
        buttonBlockCard.setIcon(new Icon(VaadinIcon.BAN));
        buttonBlockCard.setIconAfterText(true);
        buttonBlockCard.addClickListener(click -> {
            Optional<CreditCard> selectedCard = crud.getGrid().getSelectionModel().getFirstSelectedItem();
            if (selectedCard.isPresent()) {
                tryBlockOrUnblockCard(selectedCard.get());
            } else {
                Notification.show("Zaznacz kartę!", 5000, Notification.Position.MIDDLE);
            }
        });

        crud.getCrudLayout().addToolbarComponent(buttonBlockCard);

        crud.setAddOperation(chosenCreditCard -> {
            if (chosenCreditCard.getLimitAmount() == 0) {
                Notification.show("Kwota limitu musi być większa od 0!");
                return null;
            } else if(chosenCreditCard.getStartDate() == null) {
                Notification.show("Musisz wybrać datę rozpoczęcia!");
                return null;
            } else {
                CreditCard creditCard = new CreditCard();
                creditCard.setLimitAmount(chosenCreditCard.getLimitAmount());
                creditCard.setStartDate(chosenCreditCard.getStartDate());
                creditCard.setReturnDate(calculateReturnDate(chosenCreditCard));
                creditCard.setAccount(activeUser.getAccount());
                creditCard.setSubmission(new Submission());

                creditCardService.save(creditCard);
                //incrementNumberOfCreditCardsInClientAccount();
                checkIfNumberOfCreditCardsIsMax();

                Notification.show("Pomyślnie utworzono kartę kredytową!", 3000, Notification.Position.MIDDLE);
                return creditCard;
            }
        });

        crud.setFindAllOperationVisible(false);
        crud.setUpdateOperationVisible(false);
        crud.setDeleteOperationVisible(false);
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
        Optional<User> fetchedUpdatedUser = userService.get(userId);
        if (fetchedUpdatedUser.isPresent()) {
            activeUser = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    private void tryBlockOrUnblockCard(CreditCard creditCard) {
        creditCard.setBlocked(!creditCard.isBlocked());
        creditCardService.update(creditCard);
        crud.refreshGrid();
    }

    private LocalDate calculateReturnDate(CreditCard chosenCreditCard) {
        return chosenCreditCard.getStartDate().plusMonths(1L);
    }


    private void checkIfNumberOfCreditCardsIsMax() {
        if (activeUser.getAccount().getNumberOfCreditCards() == MAX_NUMBER_OF_CREDIT_CARDS)
            crud.setAddOperationVisible(false);
    }

}
