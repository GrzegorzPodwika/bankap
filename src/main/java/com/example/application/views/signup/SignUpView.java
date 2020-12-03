package com.example.application.views.signup;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

@Route(value = "signup", layout = MainView.class)
@PageTitle("SignUp")
public class SignUpView extends Div {

    public SignUpView() {
        setId("sign-up-view");
        add(new Text("Content placeholder"));
    }

}
