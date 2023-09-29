package com.infoworks.lab.domain.repository;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.layouts.RoutePath;
import com.infoworks.lab.rest.models.Response;
import com.vaadin.flow.component.UI;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class AuthRepository {

    public static boolean isUnauthorizedAccess(Response response) {
        if (response.getStatus() == 401) {
            UI.getCurrent().navigate(RoutePath.LOGIN_VIEW);
            return true;
        }
        return false;
    }

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_RESET_TOKEN = "Reset-Pass-Token";

    public static boolean matchAnyRole(String...anyRoles) {
        Optional<Object> optToken = Optional.ofNullable(UI.getCurrent().getSession().getAttribute(X_AUTH_TOKEN));
        if (optToken.isPresent()) {
            String token = optToken.get().toString();
            JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
            String userHasRoles = payload.getData().get("roles");
            if (userHasRoles == null || userHasRoles.isEmpty()) return false;
            final String userHasRolesUP = userHasRoles.toUpperCase();
            return Stream.of(anyRoles)
                    .anyMatch(role -> userHasRolesUP.contains(role.toUpperCase()));
        }
        return false;
    }

    public void doLogin(String username , String password, BiConsumer<Boolean, String> consumer) {
        //TODO:
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
            if(consumer != null)
                consumer.accept(true, "pass-auth-token");
        }else {
            if(consumer != null)
                consumer.accept(false, null);
        }
    }

    public void doLogout(String token, BiConsumer<Boolean, String> consumer){
        if(consumer != null)
            consumer.accept(true, "Successfully Logout");
    }
}
