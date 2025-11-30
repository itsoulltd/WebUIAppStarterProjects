package com.infoworks.components.ui;

import com.infoworks.applayouts.RoutePath;
import com.infoworks.config.UserSessionManagement;
import com.infoworks.domain.repositories.AuthRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;

import java.util.Objects;

/**
 * The Login view contains a button and a click listener.
 */
@Route(value = RoutePath.LOGIN_VIEW)
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
public class LoginView extends VerticalLayout {

    public LoginView() {
        Component component = loginWindowInit();
        add(component);
        setSizeFull();
        setHorizontalComponentAlignment(Alignment.CENTER , component);
    }

    private Component loginWindowInit () {
        LoginForm login = new LoginForm();
        login.setForgotPasswordButtonVisible(true);
        initListeners(login);
        //Extension:
        Button registerButton = new Button("Registration Form"
                , (event) -> UI.getCurrent().navigate(RoutePath.REGISTRATION_VIEW));
        HorizontalLayout extraButtons = new HorizontalLayout(registerButton);
        //Layout:
        VerticalLayout layout = new VerticalLayout(login, extraButtons);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }

    private void initListeners(LoginForm login){
        //On-Login-Click:
        login.addLoginListener(loginEvent -> {
            AuthRepository authRepo = new AuthRepository();
            authRepo.doLogin(loginEvent.getUsername(), loginEvent.getPassword(), (isSuccess, authToken) -> {
                if(isSuccess && Objects.nonNull(authToken)){
                    UserSessionManagement.handleSessionInitEvent(authToken);
                }else {
                    loginEvent.getSource().setError(true);
                }
            });
        });
        //On-ForgetPassword-Click:
        login.addForgotPasswordListener(event -> {
            //TODO: Implement A ForgotPassView
            Notification notification = Notification.show("Forgot Password Clicked!");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        });
    }

}
