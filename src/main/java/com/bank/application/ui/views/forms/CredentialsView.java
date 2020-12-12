package com.bank.application.ui.views.forms;

import com.bank.application.backend.service.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.Arrays;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.*;

@Route("credentials")
@PageTitle("Credentials | BankAP")
//@CssImport("./styles/views/register/register-view.css")
public class CredentialsView extends Composite {
    private final AuthService authService;

    public CredentialsView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {
        EmailField emailField = createEmail();
        TextField phone = createPhone();
        TextField id = createID();
        TextField homeAddress = new TextField("Home Address");
        homeAddress.setPlaceholder("Home Address");
        DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setLabel("Date of Birth");

        VerticalLayout verticalLayout = new VerticalLayout(
                new H1("Credentials"),
                emailField,
                phone,
                id,
                homeAddress,
                dateOfBirth,
                new Button("Sign up", e -> register(
                        emailField.isEmpty(),
                        phone.isEmpty(),
                        id.isEmpty(),
                        homeAddress.isEmpty(),
                        dateOfBirth.isEmpty()
                ))
        );
        verticalLayout.setSizeFull();
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return verticalLayout;
    }

    private void register(Boolean emailField, Boolean phone, Boolean id, Boolean homeAddress, Boolean dateOfBirth) {
        Boolean[] checkArray = new Boolean[]{emailField, phone, id, homeAddress, dateOfBirth};
        if(Arrays.asList(checkArray).contains(false)) {
            Notification.show("All fields are required");
        }
        else {
            Notification.show("Credentials accepted");
            // TODO: bind values
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                UI.getCurrent().navigate("login");
            }
        }
    }

    private EmailField createEmail() {
        EmailField emailField = new EmailField("Email");
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        return emailField;
    }

    private TextField createPhone() {
        TextField phone = new TextField("Phone");
        phone.setPattern("[0-9]*");
        phone.setPreventInvalidInput(true);
        phone.setMaxLength(9);
        phone.setMinLength(9);
        phone.setPlaceholder("123456789");

        return phone;
    }

    private TextField createID() {
        TextField id = new TextField("ID");
        id.setPattern("[0-9]*");
        id.setPreventInvalidInput(true);
        id.setMaxLength(11);
        id.setMinLength(11);
        id.setPlaceholder("Your ID");

        return id;
    }
}
