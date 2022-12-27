package com.infoworks.lab.config;

import com.infoworks.lab.components.crud.components.datasource.DefaultDataSource;
import com.infoworks.lab.components.crud.components.datasource.GridDataSource;
import com.infoworks.lab.components.db.source.JsqlDataSource;
import com.infoworks.lab.components.db.source.SqlDataSource;
import com.infoworks.lab.components.rest.RestRepositoryExecutor;
import com.infoworks.lab.components.rest.source.RestDataSource;
import com.infoworks.lab.jsql.ExecutorType;
import com.it.soul.lab.sql.entity.Entity;

import java.util.Arrays;

public interface GridDataSourceFactory {

    static GridDataSource create(ExecutorType executorType, RestRepositoryExecutor executor, Entity...samples){
        if (executorType == ExecutorType.SQL){
            //Fetching Data From Database:
            DatabaseBootstrap.createTables();
            GridDataSource source = JsqlDataSource.createDataSource(SqlDataSource.class, executorType);
            return source;
        } else if(executorType == ExecutorType.REST) {
            //Fetching Data From WebService:
            JsqlDataSource source = JsqlDataSource.createDataSource(RestDataSource.class, executorType);
            source.setExecutor(executor);
            return source;
        } else{
            //In-Memory DataSource:
            GridDataSource source = new DefaultDataSource();
            Arrays.asList(samples)
                    .stream()
                    .forEach(entity -> source.save(entity));
            return source;
        }
    }

}
