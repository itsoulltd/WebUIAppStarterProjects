package com.infoworks.lab.config;

import org.springframework.http.HttpHeaders;

import java.util.Optional;

public class RequestURI {

    public static final String OPEN_STREET_SEARCH_API = "https://nominatim.openstreetmap.org/search.php";

    public static String SCHEMA_HTTP = Optional.ofNullable(System.getenv("app.schema.http")).orElse("http://");
    public static String SCHEMA_WS = Optional.ofNullable(System.getenv("app.schema.ws")).orElse("ws://");

    public static String APP_HOST = Optional.ofNullable(System.getenv("app.api.host")).orElse("localhost");
    public static String APP_PORT = Optional.ofNullable(System.getenv("app.api.port")).orElse("8080");
    public static String APP_BASE = RequestURI.SCHEMA_HTTP + RequestURI.APP_HOST + ":" + RequestURI.APP_PORT;

    public static String AUTH_HOST = Optional.ofNullable(System.getenv("app.auth.host")).orElse("localhost");
    public static String AUTH_PORT = Optional.ofNullable(System.getenv("app.auth.port")).orElse("8080");
    public static String AUTH_BASE = RequestURI.SCHEMA_HTTP + RequestURI.AUTH_HOST + ":" + RequestURI.AUTH_PORT;
    public static String AUTH_API = Optional.ofNullable(System.getenv("app.auth.login")).orElse("/api/auth/auth/v1");

    public static String USER_HOST = Optional.ofNullable(System.getenv("app.user.host")).orElse("localhost");
    public static String USER_PORT = Optional.ofNullable(System.getenv("app.user.port")).orElse("8080");
    public static String USER_BASE = RequestURI.SCHEMA_HTTP + RequestURI.USER_HOST + ":" + RequestURI.USER_PORT;
    public static String USER_API = Optional.ofNullable(System.getenv("app.user.api")).orElse("/api/user/v1");
    public static String USER_REGISTRATION_API = Optional.ofNullable(System.getenv("app.user.registration.api")).orElse("/api/user/v1/registration");

    //public static String ABC_XY = Optional.ofNullable(System.getenv("")).orElse("");

    public static HttpHeaders createHeaderFrom(String userToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (userToken == null) return httpHeaders;
        if (userToken.startsWith("Bearer")){
            httpHeaders.set(HttpHeaders.AUTHORIZATION, userToken);
        } else {
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + userToken);
        }
        return httpHeaders;
    }
}
