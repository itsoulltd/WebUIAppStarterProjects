package com.infoworks.lab.components.presenters.GridView;

import com.infoworks.lab.beans.tasks.definition.Task;
import com.infoworks.lab.components.crud.components.views.search.SearchBarConfigurator;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.FetchItems;
import com.infoworks.lab.domain.beans.tasks.FetchItemsCount;
import com.infoworks.lab.domain.beans.tasks.SearchItems;
import com.infoworks.lab.domain.beans.tasks.rest.PagingGetTask;
import com.infoworks.lab.domain.beans.tasks.rest.SearchTask;
import com.infoworks.lab.domain.models.SecureSearchQuery;
import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.rest.models.QueryParam;
import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.models.pagination.Pagination;
import com.infoworks.lab.rest.models.pagination.SortOrder;
import com.infoworks.lab.rest.repository.RestRepository;
import com.it.soul.lab.sql.entity.EntityInterface;
import com.it.soul.lab.sql.query.models.Property;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridView<T> extends VerticalLayout implements GridFooter.ActionEvent, GridSearchView.SearchEvent {

    private static Logger LOG = Logger.getLogger("GridView");
    private Grid<T> grid;
    private GridFooter footer;
    private GridSearchView searchView;
    private RestRepository repository;
    private Class<T> type;
    private final String[] skipProperties;
    //
    private PagingGetTask fetchTask;
    private Task countTask;
    private SearchTask searchTask;

    public GridView(Class<T> type, int pageSize, RestRepository repository, String...skipProperties) {
        addClassNames("content");
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        //
        this.type = type;
        this.repository = repository;
        this.skipProperties = skipProperties;
        this.searchView = new GridSearchView(this);
        this.grid = new Grid<>(type, false);
        this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.grid.setPageSize(pageSize);
        this.footer = new GridFooter(this);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        add(searchView);
        add(grid);
        add(footer);
    }

    public Grid<T> getGrid() {
        return grid;
    }

    public GridFooter getFooter() {
        return footer;
    }

    public boolean isHideSearch() {
        return searchView.isHideSearchBar();
    }

    public void setHideSearch(boolean hideSearch) {
        searchView.setHideSearchBar(hideSearch);
    }

    public Task getFetchTask(UI ui, int page, int size, int delay) {
        if (fetchTask == null) {
            return new FetchItems(ui, this, repository, page, size, delay);
        } else {
            fetchTask.updatePageQuery(
                    new QueryParam("limit",Integer.toString(size))
                    , new QueryParam("page", Integer.toString(page)));
        }
        return fetchTask;
    }

    public void setFetchTask(PagingGetTask fetchTask) {
        this.fetchTask = fetchTask;
    }

    public Task getCountTask(UI ui) {
        if (countTask == null) {
            return new FetchItemsCount(ui, footer, repository);
        }
        return countTask;
    }

    public void setCountTask(Task countTask) {
        this.countTask = countTask;
    }

    public Task getSearchTask(UI ui, SearchQuery query, int delay) {
        if (searchTask == null) {
            return new SearchItems(ui, this, repository, query, delay);
        } else {
            searchTask.updateQuery(query);
        }
        return searchTask;
    }

    public void setSearchTask(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

    public void dispatchAsyncLoad(UI ui) {
        if (ui == null) return;
        //Dispatch async event to fetch user-list:
        //Task task = new FetchItems(ui, this, repository, 0, grid.getPageSize(), 100);
        Task task = getFetchTask(ui, 0, grid.getPageSize(), 100);
        EventQueue.dispatchTask(task);
        //Dispatch async event to fetch count:
        //Task countTask = new FetchItemsCount(ui, footer, repository);
        Task countTask = getCountTask(ui);
        EventQueue.dispatchTask(countTask);
    }

    public void setItems(List<T> items) {
        if (items == null) items = new ArrayList<>();
        //
        this.grid.setItems(items);
        if (items.isEmpty()) {
            Notification.show("Data not found!", 1000, Notification.Position.BOTTOM_END);
        }
    }

    @Override
    public void onNextArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount) {
        UI ui = event.getSource().getUI().orElse(null);
        //Task task = new FetchItems(ui, this, repository, page, pageSize, 0);
        Task task = getFetchTask(ui, page, pageSize, 0);
        EventQueue.dispatchTask(task); //Async-load and server-side push
    }

    @Override
    public void onPreviousArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount) {
        UI ui = event.getSource().getUI().orElse(null);
        //Task task = new FetchItems(ui, this, repository, page, pageSize, 0);
        Task task = getFetchTask(ui, page, pageSize, 0);
        EventQueue.dispatchTask(task); //Async-load and server-side push
    }

    @Override
    public void onSearchEvent(ClickEvent<Button> event, Property search) {
        if (search.getValue() == null || search.getValue().toString().isEmpty()) {
            LOG.log(Level.WARNING, "Search Text is Null or Empty!");
            return;
        }
        LOG.log(Level.INFO,"SearchClicked: " + search.getValue());
        if (!EntityInterface.class.isAssignableFrom(type)) {
            LOG.log(Level.WARNING, "Bean-Class Type dose not confirm to <EntityInterface>.");
            return;
        }
        List<Property> searchProps = new SearchBarConfigurator()
                .getProperties((Class<EntityInterface>)type, skipProperties);
        SecureSearchQuery query = Pagination.createQuery(SecureSearchQuery.class, grid.getPageSize(), SortOrder.DESC);
        query.setPage(0);
        query.setAuthorization(AuthRepository.parseToken());
        //Iterate Over Search-Properties:
        for (Property prop : searchProps) {
            query.getPredicate()
                    .or(prop.getKey()).isLike("%" + search.getValue().toString() + "%");
        }
        //
        UI ui = UI.getCurrent().getUI().orElse(null);
        //Task searchTask = new SearchItems(ui, this, repository, query, 0);
        Task searchTask = getSearchTask(ui, query, 0);
        EventQueue.dispatchTask(searchTask);
    }

    @Override
    public void onClearEvent(ClickEvent<Button> event) {
        //Reload:-
        footer.reset();
        dispatchAsyncLoad(UI.getCurrent().getUI().orElse(null));
    }

    public void addNewItemEventListener(GridSearchView.AddNewItemEventListener listener) {
        if (this.searchView != null) {
            this.searchView.addNewEventListener(listener);
        }
    }
}
