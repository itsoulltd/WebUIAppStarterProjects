package com.infoworks.lab.domain.repository;

import com.infoworks.lab.client.jersey.HttpTemplate;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class AuthRepository extends HttpTemplate<Response, Message> {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_RESET_TOKEN = "Reset-Pass-Token";

    @Override
    protected String schema() {
        return "http://";
    }

    @Override
    protected String host() {
        String host = System.getenv("app.api.host");
        return host == null ? "localhost" : host;
    }

    @Override
    protected Integer port() {
        String portStr = System.getenv("app.api.port");
        return portStr == null ? 8080 : Integer.valueOf(portStr);
    }

    @Override
    protected String api() {
        String api = System.getenv("app.api.login");
        return  api == null ? "/api/auth/auth/v1" : api;
    }

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
        try {
            String token = login(username, password);
            if (token != null && !token.isEmpty()) {
                if(consumer != null)
                    consumer.accept(true, token);
            }else {
                if(consumer != null)
                    consumer.accept(false, "Invalid token!");
            }
        } catch (Exception e) {
            if(consumer != null)
                consumer.accept(false, e.getMessage());
        }
    }

    public void doLogout(String token, BiConsumer<Boolean, String> consumer){
        if(consumer != null)
            consumer.accept(true, "Successfully Logout");
    }

    public String login(String username , String password) throws HttpInvocationException, IOException {
        //TODO: if(app.auth.disable=true)
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
            return "pass-auth-token";
        }
        return "";
    }

    public boolean isAccountExist(String username) throws IOException, HttpInvocationException {
        return false;
    }

    public boolean isCurrentTokenIsValid() throws IOException, HttpInvocationException {
        return false;
    }

    public Response isValidToken(String userToken) throws IOException, HttpInvocationException {
        return new Response().setStatus(401);
    }

    public String refreshCurrentToken() throws IOException, HttpInvocationException {
        return "";
    }

    public String refreshToken(String userToken) throws IOException, HttpInvocationException {
        return "";
    }

    public Response forget(String username) throws IOException, HttpInvocationException {
        return new Response().setStatus(401);
    }

    public String resetPassword(String forgetToken, String password) throws IOException, HttpInvocationException {
        return "";
    }

    public String changePassword(String oldPass, String newPass) throws IOException, HttpInvocationException {
        return "";
    }

    public String changePassword(String userToken, String oldPass, String newPass) throws IOException, HttpInvocationException {
        return "";
    }
}
