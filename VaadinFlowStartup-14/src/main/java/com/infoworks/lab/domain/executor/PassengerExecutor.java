package com.infoworks.lab.domain.executor;

import com.infoworks.lab.components.rest.RestRepositoryExecutor;
import com.infoworks.lab.domain.repository.PassengerRepository;
import com.infoworks.lab.rest.template.Interactor;

public class PassengerExecutor extends RestRepositoryExecutor {

    private PassengerRepository repository;

    public PassengerExecutor() {
        super(null);
    }

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

}
