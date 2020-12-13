package com.bank.application.ui.views.home;

import com.bank.application.backend.service.AuthService;
import com.bank.application.backend.service.UserService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {
    @Autowired
    private final UserService userService;

    public HomeView(UserService userService) {
        setId("home-view");
        this.userService = userService;
        Accordion accordion = new Accordion();
        VerticalLayout personalInformationLayout = new VerticalLayout();
        personalInformationLayout.add(
                new Span("Name: " + userService.getRepository().getByUsername("user1").getUsername()),
                new Span("Phone: 123456789"),
                new Span("Email: user1@gmail.com"),
                new Span("Address: Warszawska 24"),
                new Span("Pesel: 12345678911"),
                new Span("Date of Birth: 1990-01-01")
        );
        accordion.add("Personal Information", personalInformationLayout);

        VerticalLayout creditCardsLayout = new VerticalLayout();
        creditCardsLayout.add(
                new Span("Not yet implemented")
        );
        AccordionPanel creditCards = accordion.add("Credit Cards", creditCardsLayout);
        creditCards.setEnabled(false);
        add(accordion);
    }

}
