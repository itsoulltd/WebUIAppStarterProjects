package com.infoworks.lab.layouts;

public class RoutePath {
    /**
     * Vaadin-14 on-ward empty String in @Route(value = "") will be the index.html page.
     * e.g. @Route(value = RoutePath.LOGIN_VIEW) will be our index.html
     */
    public static final String LOGIN_VIEW = "";
    public static final String PASSENGERS_CRUD_VIEW = "passengers";
    public static final String PROFILE_VIEW = "profile";
    public static final String TRENDS_VIEW = "trends";
}
