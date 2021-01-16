package com.bank.application.ui.views.admin;

import com.bank.application.backend.entity.User;
import com.bank.application.backend.service.UserService;
import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Optional;

@PageTitle("ManageUsers")
@CssImport("./styles/views/admin/manage-users.css")
@Route(value = "manageUsers", layout = MainView.class)
public class ManageUsersView extends VerticalLayout {

    private final Grid<User> grid = new Grid<>(User.class);
    private final UserService userService;

    public ManageUsersView(UserService userService) {
        this.userService = userService;
        setSizeFull();

        addModifyUserButton();
        configureGrid();
        fetchAllClientsAndEmployees();
    }

    private void addModifyUserButton() {
        Button buttonModifyUser = new Button("Modify sensitive data", new Icon(VaadinIcon.USER));
        buttonModifyUser.setIconAfterText(true);
        buttonModifyUser.addClickListener(click -> {
            Optional<User> selectedUser = grid.getSelectionModel().getFirstSelectedItem();
            if (selectedUser.isPresent())
                showEditUserDialog(selectedUser.get());
            else
                Notification.show("Zaznacz usera!", 5000, Notification.Position.MIDDLE);
        });
        add(buttonModifyUser);
    }

    private void showEditUserDialog(User selectedUser) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout wrapper = new VerticalLayout(
                new H4("Modify data")
        );

        Label login = new Label("Username " + selectedUser.getUsername());
        Label pesel = new Label("Pesel " + selectedUser.getPesel());
        VerticalLayout oldDataLayout = new VerticalLayout(
                login,
                pesel
        );
        oldDataLayout.setId("old-data-layout");

        TextField newUsername = new TextField("New username");
        TextField newPassword = new TextField("New password");
        TextField newPesel = new TextField("New pesel");
        newPesel.setPattern("[0-9]*");
        newPesel.setPreventInvalidInput(true);
        newPesel.setMaxLength(11);
        newPesel.setPlaceholder("10109703223");

        VerticalLayout newDataLayout = new VerticalLayout(
                newUsername,
                newPassword,
                newPesel
        );

        Button confirmButton = new Button("Potwierdź", event -> {
            if (newUsername.getValue() != null && !newUsername.isEmpty())
                selectedUser.setUsername(newUsername.getValue());

            if (newPassword.getValue() != null && !newPassword.isEmpty()) {
                String passwordSalt = RandomStringUtils.random(32);
                String passwordHash = DigestUtils.sha1Hex(newPassword.getValue() + passwordSalt);

                selectedUser.setPasswordSalt(passwordSalt);
                selectedUser.setPasswordHash(passwordHash);
            }

            if (newPesel.getValue() != null && !newPesel.getValue().isEmpty()
                    && newPesel.getValue().length() == 11) {
                selectedUser.setPesel(newPesel.getValue());
            }

            userService.update(selectedUser);
            fetchAllClientsAndEmployees();
            dialog.close();
            Notification.show("Pomyślnie zmieniono dane!", 3000, Notification.Position.MIDDLE);
        });

        Button cancelButton = new Button("Anuluj", event -> {
            dialog.close();
        });


        wrapper.add(new HorizontalLayout(oldDataLayout, newDataLayout));
        wrapper.add(new HorizontalLayout(confirmButton, cancelButton));

        wrapper.setAlignItems(Alignment.CENTER);

        dialog.add(wrapper);
        dialog.open();
    }

    private void configureGrid() {
        //grid.setColumns();
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        add(grid);
    }

    private void fetchAllClientsAndEmployees() {
        List<User> users = userService.getAllClientsAndEmployees();
        grid.setItems(users);
    }
}
