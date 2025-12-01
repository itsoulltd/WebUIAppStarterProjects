package com.infoworks.domain.models;

import com.infoworks.sql.query.pagination.SearchQuery;

public class SecureSearchQuery extends SearchQuery {
    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
