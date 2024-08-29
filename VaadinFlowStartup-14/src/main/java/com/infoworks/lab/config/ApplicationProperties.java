package com.infoworks.lab.config;

import java.util.Optional;

public class ApplicationProperties {

    public static Boolean IS_AUTH_DISABLE = Boolean.parseBoolean(
            Optional.ofNullable(
                    System.getProperty("app.auth.disable") != null
                            ? System.getProperty("app.auth.disable")
                            : System.getenv("app.auth.disable")
            ).orElse("false")
    );

    public static String GOOGLE_MAP_API_KEY = Optional.ofNullable(
            System.getProperty("google.maps.api") != null
                    ? System.getProperty("google.maps.api")
                    : System.getenv("google.maps.api")
    ).orElse(null);

    public static String APP_DISPLAY_NAME = Optional.ofNullable(
            System.getProperty("app.display.name") != null
                    ? System.getProperty("app.display.name")
                    : System.getenv("app.display.name")
    ).orElse("MyApp");

    public static String APP_DISPLAY_VERSION = Optional.ofNullable(
            System.getProperty("app.display.version") != null
                    ? System.getProperty("app.display.version")
                    : System.getenv("app.display.version")
    ).orElse("v1.0");

    public static String APP_DISPLAY_MODE = Optional.ofNullable(
            System.getProperty("app.display.mode") != null
                    ? System.getProperty("app.display.mode")
                    : System.getenv("app.display.mode")
    ).orElse("Development");

    /*public static String ABC_XY = Optional.ofNullable(
            System.getProperty("") != null ? System.getProperty("") : System.getenv("")
    ).orElse("");*/
}
