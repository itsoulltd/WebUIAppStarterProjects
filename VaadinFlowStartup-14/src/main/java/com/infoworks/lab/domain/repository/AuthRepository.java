package com.infoworks.lab.domain.repository;

import java.util.function.BiConsumer;

public class AuthRepository {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String X_RESET_TOKEN = "Reset-Pass-Token";

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
