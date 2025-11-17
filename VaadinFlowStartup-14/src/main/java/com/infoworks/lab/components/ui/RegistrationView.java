package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.presenters.Forms.RegistrationForm;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = RoutePath.REGISTRATION_VIEW)
@Theme(Lumo.class)
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
public class RegistrationView extends VerticalLayout {

    public RegistrationView() {
        RegistrationForm form = new RegistrationForm();
        add(form);
    }

}
