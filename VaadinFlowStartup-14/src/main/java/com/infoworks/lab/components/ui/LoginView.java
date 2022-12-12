package com.infoworks.lab.components.ui;

import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Objects;

/**
 * The Login view contains a button and a click listener.
 */
@Route(value = "")
@Theme(Lumo.class)
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
public class LoginView extends VerticalLayout {

    private LoginForm login ;

    public LoginView() {
        loginWindowInit();
        initListeners();
        add(login);
        setSizeFull();
        setHorizontalComponentAlignment(Alignment.CENTER ,login);
    }

    private void loginWindowInit () {
        this.login = new LoginForm();
        this.login.setForgotPasswordButtonVisible(true);
    }

    private void initListeners(){
        //On-Login-Click:
        this.login.addLoginListener(loginEvent -> {
            AuthRepository authRepo = new AuthRepository();
            authRepo.doLogin(loginEvent.getUsername(), loginEvent.getPassword(), (isSuccess, authToken) -> {
                if(isSuccess && Objects.nonNull(authToken)){
                    UI.getCurrent().getSession().setAttribute("X-AUTH-TOKEN", authToken);
                    UI.getCurrent().navigate(RoutePath.PROFILE_VIEW);
                }else {
                    loginEvent.getSource().setError(true);
                }
            });
        });
        //On-ForgetPassword-Click:
        this.login.addForgotPasswordListener(event -> {
            //TODO: Implement A ForgotPassView
            Notification notification = Notification.show("Forgot Password Clicked!");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        });
    }


}
