package com.infoworks.domain.repositories;

import com.infoworks.config.ApplicationProperties;
import com.infoworks.config.RequestURI;
import com.infoworks.config.UserRole;
import com.infoworks.domain.entities.User;
import com.infoworks.domain.jwt.JWTHeader;
import com.infoworks.domain.jwt.JWTPayload;
import com.infoworks.domain.jwt.TokenValidator;
import com.infoworks.domain.jwtoken.JWTokenProvider;
import com.infoworks.domain.jwtoken.TokenProvider;
import com.infoworks.objects.Response;
import com.infoworks.orm.Property;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class AuthRepository {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_RESET_TOKEN = "Reset-Pass-Token";
    public static final String X_USER_KEY = "X-User-Key";

    public static User currentPrincipleFromToken(UI ui, Property usernameKey) {
        User principle = new User();
        principle.setName("");
        String token = parseToken(ui);
        JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
        if (payload != null && payload.getData() != null) {
            String issuer = payload.getIss();
            principle.setName(payload.getData().getOrDefault(usernameKey.getKey(), issuer));
        }
        principle.setAuthorization(token);
        return principle;
    }

    public static String parseToken(UI ui) {
        Optional<Object> optToken = (ui != null)
                ? Optional.ofNullable(ui.getSession().getAttribute(X_AUTH_TOKEN))
                : Optional.ofNullable(null);
        return optToken.isPresent() ? optToken.get().toString() : null;
    }

    public static boolean isAdmin(UI ui) {
        return matchAnyRole(ui, UserRole.ADMIN.roles());
    }

    public static boolean isTenant(UI ui) {
        return matchAnyRole(ui, UserRole.TENANT.roles());
    }

    public static boolean isUser(UI ui) {
        return matchAnyRole(ui, UserRole.USER.roles());
    }

    public static boolean matchAnyRole(UI ui, String...anyRoles) {
        if (ui == null) return false;
        Optional<Object> optToken = Optional.ofNullable(ui.getSession().getAttribute(X_AUTH_TOKEN));
        if (optToken.isPresent()) {
            String token = optToken.get().toString();
            return matchAnyRole(token, anyRoles);
        }
        return false;
    }

    public static boolean matchAnyRole(String token, String...anyRoles) {
        if (token != null) {
            JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
            String userHasRoles = payload.getData().get("roles");
            if (userHasRoles == null || userHasRoles.isEmpty()) return false;
            final String userHasRolesUP = userHasRoles.toUpperCase();
            return Stream.of(anyRoles)
                    .anyMatch(role -> userHasRolesUP.contains(role.toUpperCase()));
        }
        return false;
    }

    public boolean isAuthDisable(String username, String password) {
        return ApplicationProperties.IS_AUTH_DISABLE;
        //if(app.auth.disable==true)
        /*return ApplicationProperties.IS_AUTH_DISABLE &&
                ((username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin"))
                        || (username.equalsIgnoreCase("tenant") && password.equalsIgnoreCase("tenant")));*/
    }

    public String generateTokenWhenSecurityIsDisable(String username, String password) {
        //Return jwt-test-token for admin/admin or tenant/tenant
        if (username.equalsIgnoreCase("admin")) {
            return generateJWToken(username, UserRole.ADMIN.roles());
        } else if (username.equalsIgnoreCase("tenant")) {
            return generateJWToken(username, UserRole.TENANT.roles());
        } else {
            return generateJWToken(username, UserRole.USER.roles());
        }
    }

    private String generateJWToken(String username, String...roles) {
        JWTHeader header = new JWTHeader().setAlg("HS256").setTyp("JWT");
        JWTPayload payload = new JWTPayload()
                .setIss(username)
                .addData("roles", String.join(",", roles))
                .addData("username", username);
        TokenProvider provider = new JWTokenProvider(UUID.randomUUID().toString())
                .setPayload(payload).setHeader(header);
        String token = provider.generateToken(TokenProvider.defaultTokenTimeToLive());
        return token;
    }

    public static void saveUser(UI ui, User user) {
        ui.getSession().setAttribute(X_USER_KEY, user);
    }

    public static User retrieveUser(UI ui) {
        Object obj = ui.getSession().getAttribute(X_USER_KEY);
        return (obj instanceof User) ? (User) obj : null;
    }

    protected String schema() {
        return RequestURI.SCHEMA_HTTP;
    }

    protected String host() {
        return RequestURI.AUTH_HOST;
    }

    protected Integer port() {
        return Integer.valueOf(RequestURI.AUTH_PORT);
    }

    protected String api() {
        return RequestURI.AUTH_API;
    }

    public void doLogin(String username , String password, BiConsumer<Boolean, String> consumer) {
        //Check Auth Disable or Not:
        try {
            String token = isAuthDisable(username, password)
                    ? generateTokenWhenSecurityIsDisable(username, password)
                    : login(username, password);
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

    public String login(String username , String password) throws IOException {
        return "";
    }

    public boolean isAccountExist(String username) throws IOException {
        return false;
    }

    public boolean isCurrentTokenIsValid() throws IOException {
        return false;
    }

    public Response isValidToken(String userToken) throws IOException {
        return new Response().setStatus(401);
    }

    public String refreshCurrentToken() throws IOException {
        return "";
    }

    public String refreshToken(String userToken) throws IOException {
        return "";
    }

    public Response forget(String username) throws IOException {
        return new Response().setStatus(401);
    }

    public String resetPassword(String forgetToken, String password) throws IOException {
        return "";
    }

    public String changePassword(String oldPass, String newPass) throws IOException {
        return "";
    }

    public String changePassword(String userToken, String oldPass, String newPass) throws IOException {
        return "";
    }
}
