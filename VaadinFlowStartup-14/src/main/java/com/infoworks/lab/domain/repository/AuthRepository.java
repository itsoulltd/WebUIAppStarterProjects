package com.infoworks.lab.domain.repository;

import com.infoworks.lab.client.jersey.HttpTemplate;
import com.infoworks.lab.config.ApplicationProperties;
import com.infoworks.lab.config.RequestURI;
import com.infoworks.lab.config.UserRole;
import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class AuthRepository extends HttpTemplate<Response, Message> {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_RESET_TOKEN = "Reset-Pass-Token";

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
        //if(app.auth.disable==true)
        return ApplicationProperties.IS_AUTH_DISABLE &&
                ((username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin"))
                        || (username.equalsIgnoreCase("tenant") && password.equalsIgnoreCase("tenant")));
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

    @Override
    protected String schema() {
        return RequestURI.SCHEMA_HTTP;
    }

    @Override
    protected String host() {
        return RequestURI.AUTH_HOST;
    }

    @Override
    protected Integer port() {
        return Integer.valueOf(RequestURI.AUTH_PORT);
    }

    @Override
    protected String api() {
        return RequestURI.AUTH_API;
    }

    @Override
    protected synchronized String domain() throws MalformedURLException {
        return RequestURI.AUTH_BASE;
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

    public String login(String username , String password) throws HttpInvocationException, IOException {
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
