package com.infoworks.lab.domain.repository;

import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.rest.template.Interactor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.List;

public class UserRepositoryTest {

    private UserRepository repository;

    public UserRepository getRepository() {
        if (repository == null){
            try {
                repository = Interactor.create(UserRepository.class);
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
        env.set("app.user.host", "localhost");
        env.set("app.user.port", "8080");
        env.set("app.user.api", "/user");
    }

    @Test
    public void envTest(){
        Assert.assertTrue(System.getenv("app.user.host").equalsIgnoreCase("localhost"));
        Assert.assertTrue(System.getenv("app.user.port").equalsIgnoreCase("8080"));
        Assert.assertTrue(System.getenv("app.user.api").equalsIgnoreCase("/user"));
    }

    @Test
    public void rowCount() {
        ItemCount count = getRepository().rowCount();
        System.out.println(count.getCount());
    }

    @Test
    public void fetch() {
        ItemCount count = getRepository().rowCount();
        int max = count.getCount().intValue();
        int limit = 5;
        int page = 0;
        int numOfPage = (max / limit) + 1;
        while (page < numOfPage){
            List<User> riders = getRepository().fetch(page, limit);
            riders.forEach(rider -> System.out.println(rider.getName()));
            page++;
        }
    }

    @Test
    public void doa() {
        //Create & Insert:
        User toBeCreated = new User("Tictoc", Gender.NONE, 18);
        toBeCreated.setAuthorization("xxx-TOKEN-xxx");

        User created = getRepository().insert(toBeCreated);
        if(created != null) {
            System.out.println("Created: " + created.getName());
            //Update:
            created.setName("Tictoc-up");
            created.setAuthorization("xxx-TOKEN-xxx");
            User update = getRepository().update(created, created.getId());
            if (update != null){
                System.out.println("Updated: " + update.getName());
                //Delete:
                boolean isDeleted = getRepository().delete(update.getId(), "xxx-TOKEN-xxx");
                System.out.println("Is Deleted : " + isDeleted);
            }
        }
    }
}