package com.bank.application.backend.service;

import com.bank.application.backend.entity.Account;
import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.repository.AccountRepository;
import com.bank.application.backend.repository.UserRepository;
import com.bank.application.other.Constants;
import com.bank.application.ui.views.admin.AdminView;
import com.bank.application.ui.views.cards.CardsView;
import com.bank.application.ui.views.employee.EmployeeView;
import com.bank.application.ui.views.employee.ManagePaymentsEmployeeView;
import com.bank.application.ui.views.home.HomeView;
import com.bank.application.ui.views.main.MainView;
import com.bank.application.ui.views.payments.PaymentsView;
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
    private VaadinSession session;

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
            routes.add(new AuthorizedRoute("payments", "Payments", PaymentsView.class));
            routes.add(new AuthorizedRoute("cards", "Cards", CardsView.class));
        } else if (role == Role.EMPLOYEE) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("managePayments", "ManagePayments", ManagePaymentsEmployeeView.class));
            routes.add(new AuthorizedRoute("employee", "Employee", EmployeeView.class));
        } else if (role == Role.ADMIN) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", AdminView.class));

        }

        return routes;
    }

    public void register(String username, String password, String firstName, String lastName,
                         String pesel, String address, String email, String phone) {
        Account account = new Account(generateRandomAccountNumber());
        User user = new User(username, password, Role.EMPLOYEE, firstName, lastName, pesel, address, email, phone);

        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);
    }

    public String getCurrentUserName() {
        return VaadinSession.getCurrent().getAttribute(User.class).getUsername();
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

    public String generateRandomAccountNumber() {
        int min = 0;
        int max = 9;
        String accountNumber = "";

        for (int i = 0; i < 26; i++) {
            int randomNum = (int)(Math.random() * (max - min + 1) + min);
            accountNumber += Integer.toString(randomNum);
        }

        return accountNumber;
    }
}
