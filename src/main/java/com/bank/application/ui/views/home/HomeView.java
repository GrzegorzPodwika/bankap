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

    public HomeView(UserService authService) {
        setId("home-view");

        createTabs(authService);
    }

    public void createTabs(UserService userService) {
        VerticalLayout personalInformationVerticalLayout = createPersonalInformationLayout(userService);

        Tab personalInformation = new Tab("Personal Information");
        Tab transactions = new Tab("Transactions");
        Tab bankAccount = new Tab("Bank Account");
        Tabs tabs = new Tabs(false, personalInformation, transactions, bankAccount);

        Div personalInformationDiv = new Div();
        personalInformationDiv.add(personalInformationVerticalLayout);
        personalInformationDiv.setVisible(false);

        Div transactionsDiv = new Div();
        transactionsDiv.setVisible(false);

        Div accountInformationDiv = new Div();
        accountInformationDiv.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(personalInformation, personalInformationVerticalLayout);
        tabsToPages.put(transactions, transactionsDiv);
        tabsToPages.put(bankAccount, accountInformationDiv);
        Div pages = new Div(personalInformationVerticalLayout, transactionsDiv, accountInformationDiv);

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
}
