package com.infoworks.config;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public class ApplicationProperties {

    public static final int APP_MAX_SIZE_IN_MB = 2;
    public static final String SAMPLE_CREATE_ORDER_XLSX = "/Download/file_example_XLSX_50.xlsx";
    public static final String SAMPLE_CREATE_ORDER_XLS = "/Download/file_example_XLS_10.xls";

    /*public static iProperties properties = iProperties.create(
            Paths.get("application.properties").toFile().getAbsolutePath()
            , null);*/

    public static Properties properties;

    static {
        Properties props = new Properties();
        try (InputStream is = ApplicationProperties.class
                .getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("application.properties not found");
            }
            props.load(is);
            properties = props;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Boolean IS_AUTH_DISABLE = Boolean.parseBoolean(
            Optional.ofNullable(
                    System.getProperty("app.auth.disable") != null
                            ? System.getProperty("app.auth.disable")
                            : properties.getProperty("app.auth.disable")
            ).orElse("false")
    );

    public static String GOOGLE_MAP_API_KEY = Optional.ofNullable(
            System.getProperty("google.maps.api") != null
                    ? System.getProperty("google.maps.api")
                    : properties.getProperty("google.maps.api")
    ).orElse(null);

    public static String APP_DISPLAY_NAME = Optional.ofNullable(
            System.getProperty("app.display.name") != null
                    ? System.getProperty("app.display.name")
                    : properties.getProperty("app.display.name")
    ).orElse("MyApp");

    public static String APP_DISPLAY_VERSION = Optional.ofNullable(
            System.getProperty("app.display.version") != null
                    ? System.getProperty("app.display.version")
                    : properties.getProperty("app.display.version")
    ).orElse("v1.0");

    public static String APP_DISPLAY_MODE = Optional.ofNullable(
            System.getProperty("app.display.mode") != null
                    ? System.getProperty("app.display.mode")
                    : properties.getProperty("app.display.mode")
    ).orElse("DEV");

    /*public static String ABC_XY = Optional.ofNullable(
            System.getProperty("") != null ? System.getProperty("") : System.getenv("")
    ).orElse("");*/
}
