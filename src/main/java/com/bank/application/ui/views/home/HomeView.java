package com.bank.application.ui.views.home;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.Transaction;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.AccountService;
import com.bank.application.backend.service.TransactionService;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.BankUtils;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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

import static com.bank.application.other.BankUtils.*;

@Route(value = "home", layout = MainView.class)
@CssImport("./styles/views/home/home-view.css")
@PageTitle("Home")
public class HomeView extends Div {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private User user;
    private final H2 balanceH2 = new H2();

    public HomeView(UserService userService, AccountService accountService, TransactionService transactionService) {
        addClassName("home-view");
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;

        fetchFreshUser();

        setUpLayoutWithUserCredentials();
    }

    private void setUpLayoutWithUserCredentials() {
        TextField labelFirstName = new TextField("Imię:");
        labelFirstName.setValue(user.getFirstName());
        labelFirstName.setReadOnly(true);

        TextField labelLastName = new TextField("Nazwisko:");
        labelLastName.setValue(user.getLastName());
        labelLastName.setReadOnly(true);

        TextField labelPesel = new TextField("Pesel:");
        labelPesel.setValue(user.getPesel());
        labelPesel.setReadOnly(true);

        TextField labelAddress = new TextField("Adres:");
        labelAddress.setValue(user.getAddress());
        labelAddress.setReadOnly(true);

        TextField labelEmail = new TextField("Email:");
        labelEmail.setValue(user.getEmail());
        labelEmail.setReadOnly(true);

        TextField labelPhone = new TextField("Numer komórkowy:");
        labelPhone.setValue(user.getPhone());
        labelPhone.setReadOnly(true);

        TextField labelBirthDate = new TextField("Data urodzenia:");
        labelBirthDate.setValue(user.getBirthDate());
        labelBirthDate.setReadOnly(true);

        VerticalLayout credentialsLayout = new VerticalLayout(
                labelFirstName,
                labelLastName,
                labelPesel,
                labelAddress,
                labelEmail,
                labelPhone,
                labelBirthDate
        );
        credentialsLayout.addClassName("credentials-form");
        credentialsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Icon balanceIcon = new Icon(VaadinIcon.MONEY_EXCHANGE);
        balanceIcon.setId("balance-icon");
        H2 balancePrefix = new H2("Saldo:");
        balanceH2.setId("balance-h2");
        balanceH2.setText(roundOff(user.getAccount().getAccountBalance()));
        H2 balanceSuffix = new H2("PLN");

        Button buttonTopUp = new Button("Doładuj konto", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonTopUp.setId("button-top-up");
        buttonTopUp.setIconAfterText(true);
        buttonTopUp.setAutofocus(true);
        buttonTopUp.addClickListener(event -> showTopUpDialog());

        HorizontalLayout balanceLayout = new HorizontalLayout(balanceIcon, balancePrefix,balanceH2, balanceSuffix,buttonTopUp);
        balanceLayout.addClassName("balance-layout");
        balanceLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        balanceLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H3 accountNumber = new H3("Numer konta: " + user.getAccount().getAccountNumber());

        VerticalLayout verticalLayout = new VerticalLayout();

        if (user.getRole() == Role.USER) {
            verticalLayout.add(balanceLayout, accountNumber);
        }

        verticalLayout.add(
                credentialsLayout
        );

        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(verticalLayout);
    }

    private void showTopUpDialog() {
        Dialog dialog = new Dialog();
        dialog.setId("dialog-top-up");
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setId("vert-layout-top-up");
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

                    double newBalance = user.getAccount().getAccountBalance() + numberFieldTopUpAmount.getValue();
                    Account userAccount = user.getAccount();

                    Transaction transaction = new Transaction();
                    transaction.setTransactionTitle(textFieldTransactionTitle.getValue());
                    transaction.setAccount(userAccount);
                    transaction.setAmount(numberFieldTopUpAmount.getValue());
                    transaction.setReceiverAccountNumber(userAccount.getAccountNumber());

                    transactionService.save(transaction);

                    balanceH2.setText(roundOff(newBalance));
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
        Optional<User> fetchedUpdatedUser = userService.get(userId);
        if (fetchedUpdatedUser.isPresent()) {
            user = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }


    public static class UserNotFoundException extends Exception {

    }
}