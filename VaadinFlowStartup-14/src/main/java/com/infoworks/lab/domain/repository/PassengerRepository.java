package com.infoworks.lab.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.QueryParam;

import java.io.IOException;
import java.util.List;

public class PassengerRepository extends EntityRestRepository<Passenger, Integer> {

    public PassengerRepository() {
        super(Passenger.class, Message.class);
    }

    @Override
    protected String host() {
        return System.getenv("app.passenger.host");
    }

    @Override
    protected Integer port() {
        return Integer.valueOf(System.getenv("app.passenger.port"));
    }

    @Override
    protected String api() {
        return System.getenv("app.passenger.api");
    }

    @Override
    public String getPrimaryKeyName() {
        return "id";
    }

    @Override
    public Class<Passenger> getEntityType() {
        return Passenger.class;
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
    protected List<Passenger> unmarshal(String json) throws IOException {
        return Message.unmarshal(new TypeReference<List<Passenger>>(){}, json);
    }
}
