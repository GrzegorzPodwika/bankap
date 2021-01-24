package com.bank.application.ui.views.forms;

import com.bank.application.backend.service.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@Route(value = "login")
@PageTitle("Login | BankAP  ")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends Div {

    public LoginView(AuthService authService) {
        setId("login-view");
        setMinWidth("40%");
        setMaxWidth("30%");

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        password.setId("login-view-password");

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        Image imageBank = new Image("images/bank_logo.png", "Bank");
        imageBank.setId("banklogo");
        wrapper.add(imageBank);

        add(
                wrapper,
                new H1("Welcome"),
                username,
                password,
                new Button("Login", event -> {
                    try {
                        authService.authenticate(username.getValue(), password.getValue());
                        UI.getCurrent().navigate("home");
                    } catch (AuthService.AuthException e) {
                        Notification.show("Wrong credentials.");
                    }
                }),
                new RouterLink("Register", RegisterView.class)
        );
    }

}
