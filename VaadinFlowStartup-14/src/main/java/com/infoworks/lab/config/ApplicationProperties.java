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

    //public static String ABC_XY = Optional.ofNullable(System.getenv("")).orElse("");
}
