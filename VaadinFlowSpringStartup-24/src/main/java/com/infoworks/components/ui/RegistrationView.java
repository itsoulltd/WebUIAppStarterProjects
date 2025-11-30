package com.infoworks.components.ui;

import com.infoworks.applayouts.RoutePath;
import com.infoworks.components.presenters.Forms.RegistrationForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;

@Route(value = RoutePath.REGISTRATION_VIEW)
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
public class RegistrationView extends VerticalLayout {

    public RegistrationView() {
        RegistrationForm form = new RegistrationForm();
        add(form);
    }

}
