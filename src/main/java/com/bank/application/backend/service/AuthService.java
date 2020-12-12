package com.bank.application.backend.service;

import com.bank.application.backend.entity.Role;
import com.bank.application.backend.entity.User;
import com.bank.application.backend.repository.UserRepository;
import com.bank.application.ui.views.admin.AdminView;
import com.bank.application.ui.views.cards.CardsView;
import com.bank.application.ui.views.employee.EmployeeView;
import com.bank.application.ui.views.home.HomeView;
import com.bank.application.ui.views.main.MainView;
import com.bank.application.ui.views.payments.PaymentsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

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

    public static class AuthException extends Exception { }

    private final UserRepository userRepository;

    public AuthService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password)) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(route.route, route.view, MainView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        List<AuthorizedRoute> routes = new ArrayList<>();

        if (role == Role.USER) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("payments", "Payments", PaymentsView.class));
            routes.add(new AuthorizedRoute("cards", "Cards", CardsView.class));
        } else if(role == Role.EMPLOYEE) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("employee", "Employee", EmployeeView.class));
        } else if(role == Role.ADMIN) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", AdminView.class));

        }

        return routes;
    }

    public void register(String username, String password) {
        userRepository.save(new User(username, password, Role.USER));
    }

}
