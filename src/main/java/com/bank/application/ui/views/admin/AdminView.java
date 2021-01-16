package com.bank.application.ui.views.admin;

import com.bank.application.backend.entity.Role;
import com.bank.application.backend.service.AuthService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;

import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.Arrays;

@PageTitle("Admin")
@CssImport("./styles/views/admin/admin-view.css")
@Route(value = "admin", layout = MainView.class)
public class AdminView extends Composite {
    private final AuthService authService;

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm Password");
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField pesel = createPesel();
    private final TextField address = new TextField("Address", "Home Address");
    private final DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private final EmailField emailField = createEmail();
    private final TextField phone = createPhone();
    private final ComboBox<String> userRole = new ComboBox<>("Role", Arrays.asList("USER", "EMPLOYEE"));

    public AdminView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {

        FormLayout formLayout = new FormLayout(
                username,
                password,
                confirmPassword,
                firstName,
                lastName,
                pesel,
                address,
                dateOfBirth,
                emailField,
                phone,
                userRole
        );

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        VerticalLayout verticalLayout = new VerticalLayout(new H1("Add user to database"),
                formLayout,
                new Button("Sign up", e -> register(
                        username.getValue(),
                        password.getValue(),
                        confirmPassword.getValue(),
                        firstName.getValue(),
                        lastName.getValue(),
                        pesel.getValue(),
                        address.getValue(),
                        emailField.getValue(),
                        phone.getValue(),
                        dateOfBirth.getValue(),
                        userRole.getValue()
                )));

        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return verticalLayout;
    }

    private void register(String username, String password, String confirmPassword, String firstName, String lastName,
                          String pesel, String address, String email, String phone, LocalDate birthDate, String role) {

        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if(password.isEmpty()) {
            Notification.show("Enter a password");
        } else if(!password.equals(confirmPassword)) {
            Notification.show("Passwords don't match!");
        } else if(firstName.isEmpty()) {
            Notification.show("Enter a firstName");
        } else if(lastName.isEmpty()) {
            Notification.show("Enter a lastName");
        } else if(address.isEmpty()) {
            Notification.show("Enter a address");
        } else if(!email.matches("^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$")){
            Notification.show("Email is not correct!");
        } else if(phone.length() != 9){
            Notification.show("Phone number is not correct!");
        } else if(pesel.length() != 11){
            Notification.show("Pesel is not correct!");
        } else if(birthDate == null){
            Notification.show("Choose birth date!");
        } else if(role == null){
            Notification.show("Choose user role!");
        } else {
            authService.register(username, password, firstName, lastName, pesel, address, email, phone, birthDate.toString(), Role.valueOf(role));
            Notification.show("Registration succeeded.", 5000, Notification.Position.MIDDLE);
            clearTextFields();
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
        phone.setPlaceholder("123456789");

        return phone;
    }

    private TextField createPesel() {
        TextField id = new TextField("Pesel");
        id.setPattern("[0-9]*");
        id.setPreventInvalidInput(true);
        id.setMaxLength(11);
        id.setPlaceholder("10109703223");

        return id;
    }

    private void clearTextFields() {
        username.clear();
        password.clear();
        confirmPassword.clear();
        firstName.clear();
        lastName.clear();
        pesel.clear();
        address.clear();
        dateOfBirth.clear();
        emailField.clear();
        phone.clear();
        userRole.clear();
    }
}
