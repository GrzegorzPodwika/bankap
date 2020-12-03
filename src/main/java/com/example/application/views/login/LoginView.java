package com.example.application.views.login;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login")
@RouteAlias(value = "", layout = MainView.class)
public class LoginView extends Div {

    public LoginView() {
        setId("login-view");
        add(new Text("Content placeholder"));
    }

}
