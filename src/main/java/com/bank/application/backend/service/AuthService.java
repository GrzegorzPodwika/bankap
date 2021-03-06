package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.repository.AccountRepository;
import com.bank.application.backend.repository.UserRepository;
import com.bank.application.other.BankUtils;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.admin.AdminView;
import com.bank.application.ui.views.admin.ManageUsersView;
import com.bank.application.ui.views.credentials.CredentialsView;
import com.bank.application.ui.views.employee.ManageCreditCardsView;
import com.bank.application.ui.views.employee.ManageCreditsView;
import com.bank.application.ui.views.employee.ManagePaymentsEmployeeView;
import com.bank.application.ui.views.home.HomeView;
import com.bank.application.ui.views.main.MainView;
import com.bank.application.ui.views.user.cards.CardsView;
import com.bank.application.ui.views.user.credit.CreditView;
import com.bank.application.ui.views.user.payments.PaymentsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AuthService(@Autowired UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password)) {
            VaadinSession.getCurrent().setAttribute(Constants.USER_ID, user.getId());
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role)
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(route.route, route.view, MainView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        List<AuthorizedRoute> routes = new ArrayList<>();

        if (role == Role.USER) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("credentials", "Credentials", CredentialsView.class));
            routes.add(new AuthorizedRoute("payments", "Payments", PaymentsView.class));
            routes.add(new AuthorizedRoute("cards", "Cards", CardsView.class));
            routes.add(new AuthorizedRoute("credits", "Credits", CreditView.class));
        } else if (role == Role.EMPLOYEE) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("credentials", "Credentials", CredentialsView.class));
            routes.add(new AuthorizedRoute("managePayments", "ManagePayments", ManagePaymentsEmployeeView.class));
            routes.add(new AuthorizedRoute("manageCredits", "ManageCredits", ManageCreditsView.class));
            routes.add(new AuthorizedRoute("manageCreditCards", "ManageCreditCards", ManageCreditCardsView.class));
        } else if (role == Role.ADMIN) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("credentials", "Credentials", CredentialsView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", AdminView.class));
            routes.add(new AuthorizedRoute("manageUsers", "ManageUsers", ManageUsersView.class));

        }

        return routes;
    }

    public void register(String username, String password, String firstName, String lastName,
                         String pesel, String address, String email, String phone, String birthDate) {
        Account account = new Account(BankUtils.generateRandomAccountNumber());
        User user = new User(username, password, Role.USER, firstName, lastName, pesel, address, email, phone, birthDate);

        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);
    }

    public void register(String username, String password, String firstName, String lastName,
                         String pesel, String address, String email, String phone, String birthDate, Role userRole) {
        Account account = new Account(BankUtils.generateRandomAccountNumber());
        User user = new User(username, password, userRole, firstName, lastName, pesel, address, email, phone, birthDate);

        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);
    }


    public static class AuthorizedRoute {
        private final String route;
        private final String name;
        private final Class<? extends Component> view;

        public AuthorizedRoute(String route, String name, Class<? extends Component> view) {
            this.route = route;
            this.name = name;
            this.view = view;
        }

        public String getRoute() {
            return route;
        }

        public String getName() {
            return name;
        }

        public Class<? extends Component> getView() {
            return view;
        }
    }

    public static class AuthException extends Exception {
    }
}
