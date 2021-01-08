package com.bank.application.ui.views.credentials;

import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.UserService;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

import static com.bank.application.ui.views.home.HomeView.UserNotFoundException;

@Route(value = "credentials", layout = MainView.class)
@PageTitle("Credentials")
public class CredentialsView extends HorizontalLayout {

    private User user;
    private final UserService userService;

    private final Label labelFirstName = new Label();
    private final Label labelLastName = new Label();
    private final Label labelPesel = new Label();
    private final Label labelAddress = new Label();
    private final Label labelEmail = new Label();
    private final Label labelPhone = new Label();

    private final TextField textFieldFirstName = new TextField("Nowe imię");
    private final TextField textFieldLastName = new TextField("Nowe nazwisko");
    private final TextField textFieldAddress = new TextField("Nowy adres");
    private final TextField textFieldEmail = new TextField("Nowy email");
    private final TextField textFieldPhone = new TextField("Nowy numer telefonu");

    public CredentialsView(UserService userService) {
        setId("payments-view");
        this.userService = userService;

        setSizeFull();
        fetchActiveUser();
        setUpLayout();
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
            user = fetchedUpdatedUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    private void setUpLayout() {
        fillUpLabelsWithUserCredentials();


        VerticalLayout vlLabels = new VerticalLayout(
            labelFirstName,
            labelLastName,
            labelPesel,
            labelAddress,
            labelEmail,
            labelPhone
        );

        vlLabels.setAlignItems(Alignment.END);

        Button buttonChangeCredentials = new Button("Zmień dane");
        buttonChangeCredentials.addClickListener(event -> {
           updateUser();
        });

        VerticalLayout vlTextFields = new VerticalLayout(
                textFieldFirstName,
                textFieldLastName,
                textFieldAddress,
                textFieldEmail,
                textFieldPhone,
                buttonChangeCredentials
        );

        add(vlLabels, vlTextFields);
    }

    private void fillUpLabelsWithUserCredentials() {
        labelFirstName.setText("First Name: " + user.getFirstName());
        labelLastName.setText("Last Name: " + user.getLastName());
        labelPesel.setText("Pesel: " + user.getPesel());
        labelAddress.setText("Address: " + user.getAddress());
        labelEmail.setText("Email: " + user.getEmail());
        labelPhone.setText("Phone: " + user.getPhone());
    }

    private void updateUser() {
        if(!textFieldFirstName.isEmpty())
            user.setFirstName(textFieldFirstName.getValue());

        if (!textFieldLastName.isEmpty())
            user.setLastName(textFieldLastName.getValue());

        if (!textFieldAddress.isEmpty())
            user.setAddress(textFieldAddress.getValue());

        if (!textFieldEmail.isEmpty())
            user.setEmail(textFieldEmail.getValue());

        if (!textFieldPhone.isEmpty())
            user.setPhone(textFieldPhone.getValue());

        user = userService.update(user);
        fillUpLabelsWithUserCredentials();
        clearTextFields();
    }

    private void clearTextFields() {
        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldAddress.clear();
        textFieldEmail.clear();
        textFieldPhone.clear();
    }

}
