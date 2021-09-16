package com.infoworks.lab.domain.executor;

import com.infoworks.lab.components.rest.RestExecutor;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.repository.PassengerRepository;
import com.infoworks.lab.rest.template.Interactor;
import com.it.soul.lab.sql.query.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PassengerExecutor extends RestExecutor {

    public PassengerExecutor() {
        super(Passenger.class, PassengerRepository.getKeyContainer());
    }
    private PassengerRepository repository;

    public PassengerRepository getRepository() {
        if (repository == null){
            try {
                repository = Interactor.create(PassengerRepository.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return repository;
    }

    @Override
    public Integer getScalarValue(SQLScalarQuery query) throws SQLException {
        return super.getScalarValue(query);
    }

    @Override
    public List executeSelect(SQLSelectQuery query, Class aClass, Map map) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        return super.executeSelect(query, aClass, map);
    }

    @Override
    public Integer executeInsert(boolean b, SQLInsertQuery query) throws SQLException, IllegalArgumentException {
        return super.executeInsert(b, query);
    }

    @Override
    public Integer executeUpdate(SQLUpdateQuery query) throws SQLException {
        return super.executeUpdate(query);
    }

    @Override
    public Integer executeDelete(SQLDeleteQuery query) throws SQLException {
        return super.executeDelete(query);
    }
}
