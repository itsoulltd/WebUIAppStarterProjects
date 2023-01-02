package com.infoworks.lab.domain.models;

import com.infoworks.lab.rest.models.Message;

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
