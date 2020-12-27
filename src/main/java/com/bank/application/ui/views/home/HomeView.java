package com.bank.application.ui.views.home;

import com.bank.application.backend.service.UserService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {

    public HomeView(UserService userService) {
        setId("home-view");

        createTabs(userService);
    }

    public void createTabs(UserService userService) {
        VerticalLayout personalInfoVerticalLayout = createPersonalInformationLayout(userService);
        VerticalLayout accountVerticalLayout = createBankAccountLayout(userService);
        VerticalLayout transactionsVerticalLayout = createTransactionsLayout(userService);

        Tab personalInformationTab = new Tab("Personal Information");
        Tab transactionsTab = new Tab("Transactions");
        Tab accountTab = new Tab("Bank Account");
        Tabs tabs = new Tabs(false, personalInformationTab, transactionsTab, accountTab);

        Div personalInformationDiv = new Div();
        Div transactionsDiv = new Div();
        Div accountDiv = new Div();

        personalInformationDiv.add(personalInfoVerticalLayout);
        transactionsDiv.add(transactionsVerticalLayout);
        accountDiv.add(accountVerticalLayout);

        personalInformationDiv.setVisible(false);
        transactionsDiv.setVisible(false);
        accountDiv.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(personalInformationTab, personalInformationDiv);
        tabsToPages.put(transactionsTab, transactionsDiv);
        tabsToPages.put(accountTab, accountDiv);
        Div pages = new Div(personalInfoVerticalLayout, transactionsDiv, accountDiv);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }

    public VerticalLayout createPersonalInformationLayout(UserService userService) {
        String username = userService.getCurrentUserName();
        String firstName = userService.getCurrentFirstName();
        String lastName = userService.getCurrentLastName();
        String pesel = userService.getCurrentPesel();
        String address = userService.getCurrentAddress();
        String phone = userService.getCurrentPhone();
        String email = userService.getCurrentEmail();

        Span usernameSpan = new Span("Username: " + username);
        Span firstNameSpan = new Span("First Name: " + firstName);
        Span lastNameSpan = new Span("Last Name: " + lastName);
        Span peselSpan = new Span("Pesel: " + pesel);
        Span addressSpan = new Span("Address: " + address);
        Span dateOfBirthSpan = new Span("Date of Birth: ");
        Span phoneSpan = new Span("Phone: " + phone);
        Span emailSpan = new Span("Email: " + email);

        VerticalLayout personalInformationVerticalLayout = new VerticalLayout();
        personalInformationVerticalLayout.add(usernameSpan, firstNameSpan, lastNameSpan, peselSpan, addressSpan,
                dateOfBirthSpan, phoneSpan, emailSpan);
        personalInformationVerticalLayout.setVisible(false);

        return personalInformationVerticalLayout;
    }

    public VerticalLayout createBankAccountLayout(UserService userService) {
        String accountNumber = userService.getCurrentUserName();
        String accountBalance = userService.getCurrentFirstName();

        Span accountNumberSpan = new Span("Account number: " + accountNumber);
        Span accountBalanceSpan = new Span("Balance: " + accountBalance);

        VerticalLayout accountVerticalLayout = new VerticalLayout();
        accountVerticalLayout.add(accountNumberSpan, accountBalanceSpan);
        accountVerticalLayout.setVisible(false);

        return accountVerticalLayout;
    }

    public VerticalLayout createTransactionsLayout(UserService userService) {
        String accountNumber = userService.getCurrentUserName();
        String accountBalance = userService.getCurrentFirstName();

        Span accountNumberSpan = new Span("Account number: " + accountNumber);
        Span accountBalanceSpan = new Span("Balance: " + accountBalance);

        VerticalLayout accountVerticalLayout = new VerticalLayout();
        accountVerticalLayout.add(accountNumberSpan, accountBalanceSpan);
        accountVerticalLayout.setVisible(false);

        return accountVerticalLayout;
    }
}
