package com.infoworks.config;

import com.infoworks.applayouts.RoutePath;
import com.infoworks.objects.Response;
import com.vaadin.flow.component.UI;

import static com.infoworks.domain.repositories.AuthRepository.X_AUTH_TOKEN;

public class UserSessionManagement {

    /**
     * Temporary solution:
     * Should be implemented in global exception handler.
     */
    public static boolean handleSessionExpireEvent(Response response) {
        if (response == null) return false;
        if (response.getStatus() == 200 || response.getStatus() == 201) return false;
        UI.getCurrent().getSession().setAttribute(X_AUTH_TOKEN, null);
        AppQueue.unregister();
        UI.getCurrent().getSession().close();
        UI.getCurrent().navigate(RoutePath.LOGIN_VIEW);
        return true;
    }

    public static boolean handleSessionInitEvent(String token) {
        if (token == null) return false;
        //TODO: For Extra security we can validate token, if we know secret key:
        //TokenProvider provider = new JWTokenProvider("secret-key");
        //if (provider.isValid(token)) return false;
        //
        UI.getCurrent().getSession().setAttribute(X_AUTH_TOKEN, token);
        AppQueue.register();
        UI.getCurrent().navigate(RoutePath.PROFILE_VIEW);
        return true;
    }

}
