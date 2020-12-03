package com.example.application.views.home;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

@Route(value = "home", layout = MainView.class)
@PageTitle("Home")
public class HomeView extends Div {

    public HomeView() {
        setId("home-view");
        add(new Text("Content placeholder"));
    }

}
