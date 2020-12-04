package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.*;

@Route("register")
@PageTitle("Register")
//@CssImport("./styles/views/register/register-view.css")
public class RegisterView extends Composite {
    private final AuthService authService;

    public RegisterView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        VerticalLayout verticalLayout = new VerticalLayout(
                new H1("Join our bank"),
                username,
                password,
                confirmPassword,
                new Button("Sign up", e -> register(
                        username.getValue(),
                        password.getValue(),
                        confirmPassword.getValue()
                ))
        );
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return verticalLayout;
    }

    private void register(String username, String password , String confirmPassword) {
        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if(password.isEmpty()) {
            Notification.show("Enter a password");
        } else if(!password.equals(confirmPassword)) {
            Notification.show("Passwords don't match!");
        } else {
            authService.register(username, password);
            Notification.show("Registration succeeded.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                UI.getCurrent().navigate("login");
            }
        }
    }
}
