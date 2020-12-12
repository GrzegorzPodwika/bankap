package com.bank.application.ui.views.home;

import com.bank.application.ui.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {

    public HomeView() {
        setId("home-view");
        add(new Text("Content placeholder"));
    }

}
