package com.bank.application.ui.views.home;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.TransactionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private User user;
    private H2 balanceH2;

    public HomeView(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;

        fetchFreshUser();

        setId("home-view");
        System.out.println(user);

        setUpLayoutWithUserCredentials();
        //createTabs();
    }

    private void setUpLayoutWithUserCredentials() {
        Label labelFirstName = new Label("First Name: " + user.getFirstName());
        Label labelLastName = new Label("Last Name: " + user.getLastName());
        Label labelPesel = new Label("Pesel: " + user.getPesel());
        Label labelAddress = new Label("Address: " + user.getAddress());
        Label labelEmail = new Label("Email: " + user.getEmail());
        Label labelPhone = new Label("Phone: " + user.getPhone());
        Label labelBirthDate = new Label("BirthDate: " + user.getBirthDate());

/*
        FormLayout credentialsLayout = new FormLayout(
                labelFirstName, labelLastName, labelPesel,
                labelAddress, labelEmail, labelPhone
        );


        credentialsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3)
        );*/

        //TODO tu można jakoś przyrzeźbić żeby to lepiej wyglądało ten ekran powitalny z saldem i user credentials
        balanceH2 = new H2("Saldo " + user.getAccount().getAccountBalance() + " PLN");
        Button buttonTopUp = new Button("Doładuj konto", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonTopUp.setIconAfterText(true);
        buttonTopUp.setAutofocus(true);
        buttonTopUp.addClickListener(event -> showTopUpDialog());

        HorizontalLayout horizontalLayout = new HorizontalLayout(balanceH2, buttonTopUp);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H3 accountNumber = new H3("Numer konta " + user.getAccount().getAccountNumber());

        VerticalLayout verticalLayout = new VerticalLayout();

        if (user.getRole() == Role.USER) {
            verticalLayout.add(horizontalLayout, accountNumber);
        }

        verticalLayout.add(
                labelFirstName,
                labelLastName,
                labelPesel,
                labelAddress,
                labelEmail,
                labelPhone,
                labelBirthDate
        );

        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(verticalLayout);
    }

    private void showTopUpDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout verticalLayout = new VerticalLayout();
        NumberField numberFieldTopUpAmount = new NumberField("Wartość wpłaty");
        TextField textFieldTransactionTitle = new TextField("Tytuł wpłaty");

        verticalLayout.add(
                new Text("Doładuj konto"),
                textFieldTransactionTitle,
                numberFieldTopUpAmount);

        Button confirmButton = new Button("Potwierdź", event -> {
            if (numberFieldTopUpAmount.getValue() == 0.0){
                numberFieldTopUpAmount.setErrorMessage("Wartość nie może byc 0!");
            } else {
                if (!textFieldTransactionTitle.getValue().isEmpty()) {
                    double newBalance = Double.parseDouble(user.getAccount().getAccountBalance()) + numberFieldTopUpAmount.getValue();
                    newBalance =  Math.round(newBalance*100) / 100.0;
                    Account userAccount = user.getAccount();
                    userAccount.setAccountBalance(Double.toString(newBalance));

                    Transaction transaction = new Transaction();
                    transaction.setTransactionTitle(textFieldTransactionTitle.getValue());
                    transaction.setAccount(userAccount);
                    transaction.setAmount(numberFieldTopUpAmount.getValue());
                    transaction.setReceiverAccountNumber(userAccount.getAccountNumber());

                    accountService.update(userAccount);
                    transactionService.save(transaction);

                    balanceH2.setText("Saldo " + newBalance + " PLN");
                    dialog.close();
                } else {
                    textFieldTransactionTitle.setErrorMessage("Tytuł wpłaty nie może być pusty");
                }
            }
        });

        Button cancelButton = new Button("Anuluj", event -> {
            dialog.close();
        });

        verticalLayout.add(new HorizontalLayout(confirmButton, cancelButton));
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);


        dialog.add(verticalLayout);
        dialog.open();
    }

    private void fetchFreshUser() {
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
            user = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }


    public static class UserNotFoundException extends Exception {

    }
}