package com.infoworks.lab.domain.models;

import com.infoworks.lab.rest.models.SearchQuery;

public class SecureSearchQuery extends SearchQuery {
    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
