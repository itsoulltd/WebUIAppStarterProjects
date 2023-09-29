package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.crud.Configurator;
import com.infoworks.lab.components.crud.Crud;
import com.infoworks.lab.components.crud.components.datasource.GridDataSource;
import com.infoworks.lab.components.crud.components.utils.EditorDisplayType;
import com.infoworks.lab.components.crud.components.views.search.PropertySearchBar;
import com.infoworks.lab.components.crud.components.views.search.SearchBar;
import com.infoworks.lab.components.crud.components.views.search.SearchBarConfigurator;
import com.infoworks.lab.components.presenters.PassengerEditor;
import com.infoworks.lab.config.GridDataSourceFactory;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.executor.PassengerExecutor;
import com.infoworks.lab.jsql.ExecutorType;
import com.infoworks.lab.layouts.RootAppLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = RoutePath.PASSENGERS_CRUD_VIEW, layout = RootAppLayout.class)
public class PassengersView extends Composite<Div> implements Crud.EventListener<Passenger> {

    private SearchBar<Passenger> searchBar;

    public PassengersView() {
        super();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        System.out.println("onAttach");
        //
        if (getContent().getChildren().count() > 0){
            getContent().removeAll();
        }
        //Create DataSource:
        GridDataSource source = GridDataSourceFactory.create(ExecutorType.IN_MEM
                , new PassengerExecutor()
                , getPassengers().toArray(new Passenger[0]));

        SearchBarConfigurator searchConfig = new SearchBarConfigurator()
                .setSkipProperties("id", "active")
                .setHideAddNewButton(false);
        searchBar = new PropertySearchBar<>(Passenger.class, searchConfig);

        Configurator configurator = new Configurator(Passenger.class)
                .setDisplayType(EditorDisplayType.EMBEDDED)
                .setDataSource(source)
                .setEditor(PassengerEditor.class)
                .setDialog(PassengerEditor.class)
                .setSearchBar(searchBar)
                .setGridPageSize(8);

        Crud crud = new Crud(configurator);
        getContent().add(crud);
    }

    private List<Passenger> getPassengers() {
        List<Passenger> personList = new ArrayList<>();
        personList.add(new Passenger("Lucas", Gender.MALE, 68));
        personList.add(new Passenger("Peter", Gender.MALE, 38));
        personList.add(new Passenger("Jack", Gender.MALE, 28));
        personList.add(new Passenger("Samuel", Gender.MALE, 53));
        return personList;
    }

    @Override
    public void onSaveSuccess(Passenger passenger, Object o) {
        searchBar.clearSearchBarView();
    }

    @Override
    public void onDeleteSuccess(Passenger passenger) {
        searchBar.clearSearchBarView();
    }

}
