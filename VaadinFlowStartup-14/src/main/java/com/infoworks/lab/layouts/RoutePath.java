package com.infoworks.lab.layouts;

public class RoutePath {
    /**
     * Vaadin-14 on-ward empty String in @Route(value = "") will be the index.html page.
     * e.g. @Route(value = RoutePath.LOGIN_VIEW) will be our index.html
     */
    public static final String LOGIN_VIEW = "";
    public static final String USERS_CRUD_VIEW = "users";
    public static final String PROFILE_VIEW = "profile";
    public static final String TRENDS_VIEW = "trends";
    public static final String GEO_TRACKER_VIEW = "geo-tracker";
}
