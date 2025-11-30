package com.infoworks.domain.models;

import com.infoworks.objects.Message;

public class Authorization extends Message {

    private String authorization;

    public Authorization(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
