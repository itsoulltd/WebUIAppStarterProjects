package com.infoworks.lab.domain.repository;

import com.infoworks.lab.rest.template.Interactor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class PassengerRepositoryTest {

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

    @Rule
    public final EnvironmentVariables env = new EnvironmentVariables();

    @Before
    public void before() {
        env.set("app.rider.host", "localhost");
        env.set("app.rider.port", "8080");
        env.set("app.rider.api", "rider");
    }

    @Test
    public void envTest(){
        Assert.assertTrue(System.getenv("app.rider.host").equalsIgnoreCase("localhost"));
        Assert.assertTrue(System.getenv("app.rider.port").equalsIgnoreCase("8080"));
        Assert.assertTrue(System.getenv("app.rider.api").equalsIgnoreCase("rider"));
    }

    @Test
    public void rowCount() {
    }

    @Test
    public void fetch() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}