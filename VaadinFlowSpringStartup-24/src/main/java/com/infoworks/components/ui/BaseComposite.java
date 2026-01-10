package com.infoworks.components.ui;

import com.infoworks.config.UserSessionManagement;
import com.infoworks.domain.repositories.AuthRepository;
import com.infoworks.objects.Response;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class BaseComposite<D extends Div> extends Composite<D> implements BeforeEnterObserver {

    private AuthRepository repository;

    public BaseComposite(AuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(UI.getCurrent().getSession().getAttribute(AuthRepository.X_AUTH_TOKEN) == null) {
            UserSessionManagement.handleSessionExpireEvent(new Response().setStatus(401));
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Object token = UI.getCurrent().getSession().getAttribute(AuthRepository.X_AUTH_TOKEN);
        if (token == null) {
            UserSessionManagement.handleSessionExpireEvent(new Response().setStatus(401));
        } else {
            if (repository.isAuthDisable("admin", "admin")) {
                return;
            }
            //On every tab-refresh checking token validation would be more secure:
            //Which shall be causes too many network calls.
            try {
                Response response = repository.isValidToken((String) token);
                UserSessionManagement.handleSessionExpireEvent(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
        }
    }
}
