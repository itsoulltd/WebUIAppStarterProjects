package com.infoworks.lab.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.QueryParam;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserRepository extends EntityRestRepository<User, Integer> {

    public UserRepository() {
        super(User.class, Message.class);
    }

    @Override
    protected String host() {
        return Optional.ofNullable(System.getenv("app.user.host")).orElse("localhost");
    }

    @Override
    protected Integer port() {
        String portStr = Optional.ofNullable(System.getenv("app.user.port")).orElse("8080");
        return Integer.valueOf(portStr);
    }

    @Override
    protected String api() {
        return Optional.ofNullable(System.getenv("app.user.api")).orElse("user");
    }

    @Override
    public String getPrimaryKeyName() {
        return "id";
    }

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    public boolean delete(Integer userId, String token){
        try {
            Authorization authorization = new Authorization(token);
            boolean isDeleted = delete(authorization, new QueryParam("userid", userId.toString()));
            return isDeleted;
        } catch (HttpInvocationException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected List<User> unmarshal(String json) throws IOException {
        if (json.startsWith("{")) {
            return Arrays.asList(Message.unmarshal(User.class, json));
        } else {
            return Message.unmarshal(new TypeReference<List<User>>(){}, json);
        }
    }
}
