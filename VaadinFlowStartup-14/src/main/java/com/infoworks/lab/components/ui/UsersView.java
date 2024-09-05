package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.crud.Configurator;
import com.infoworks.lab.components.crud.Crud;
import com.infoworks.lab.components.crud.components.datasource.GridDataSource;
import com.infoworks.lab.components.crud.components.utils.EditorDisplayType;
import com.infoworks.lab.components.crud.components.views.search.SearchBar;
import com.infoworks.lab.components.crud.components.views.search.SearchBarConfigurator;
import com.infoworks.lab.components.presenters.UserEditor;
import com.infoworks.lab.config.GridDataSourceFactory;
import com.infoworks.lab.domain.entities.User;
import com.infoworks.lab.domain.executor.RepositoryExecutor;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.domain.repository.UserRepository;
import com.infoworks.lab.jsql.ExecutorType;
import com.infoworks.lab.layouts.ApplicationLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = RoutePath.USERS_CRUD_VIEW, layout = ApplicationLayout.class)
public class UsersView extends Composite<Div> implements Crud.EventListener<User>{

    private SearchBar<User> iSearchBar;

    public UsersView() {
        getContent().add(new Span("Users"));
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
        String token = AuthRepository.parseToken(UI.getCurrent());
        GridDataSource source = GridDataSourceFactory.create(ExecutorType.IN_MEM
                , new RepositoryExecutor(new UserRepository(), token)
                , getUsers().toArray(new User[0]));

        SearchBarConfigurator searchConfig = new SearchBarConfigurator()
                .setSkipProperties("id")
                .setHideAddNewButton(false);
        //iSearchBar = new PropertySearchBar(User.class, searchConfig);
        iSearchBar = new SearchBar(User.class, searchConfig);

        Configurator configurator = new Configurator(User.class)
                .setDisplayType(EditorDisplayType.COMBINED)
                .setDataSource(source)
                .setEditor(UserEditor.class)
                .setDialog(UserEditor.class)
                .setSearchBar(iSearchBar)
                .setHideSearchBar(false)
                .setGridPageSize(8);

        Crud crud = new Crud(configurator)
                .addEventListener(this);
        crud.getGrid().setAllRowsVisible(true);//For Dynamic Table Height.
        crud.getGrid().addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        getContent().add(crud);
    }

    private List<User> getUsers() {
        List<User> personList = new ArrayList<>();
        personList.add(new User("Lucas", Gender.MALE, 68));
        personList.add(new User("Peter", Gender.MALE, 38));
        personList.add(new User("Jack", Gender.MALE, 28));
        personList.add(new User("Samuel", Gender.MALE, 53));
        return personList;
    }

    @Override
    public void onSaveSuccess(User user, Object o) {
        if(iSearchBar != null) iSearchBar.clearSearchBarView();
    }

    @Override
    public void onDeleteSuccess(User user) {
        if(iSearchBar != null) iSearchBar.clearSearchBarView();
    }
}
