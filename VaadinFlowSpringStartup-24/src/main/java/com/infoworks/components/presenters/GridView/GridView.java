package com.infoworks.components.presenters.GridView;

import com.infoworks.config.AppQueue;
import com.infoworks.domain.tasks.PagingGetTask;
import com.infoworks.domain.tasks.SearchTask;
import com.infoworks.domain.models.SecureSearchQuery;
import com.infoworks.domain.repositories.AuthRepository;
import com.infoworks.orm.Property;
import com.infoworks.sql.query.pagination.SearchQuery;
import com.infoworks.sql.query.pagination.Pagination;
import com.infoworks.sql.query.pagination.SortOrder;
import com.infoworks.entity.Entity;
import com.infoworks.tasks.Task;
import com.infoworks.utils.rest.client.GetTask;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridView<T> extends VerticalLayout implements GridFooter.ActionEvent, GridSearchView.SearchEvent {

    private static Logger LOG = Logger.getLogger("GridView");
    private Grid<T> grid;
    private GridFooter footer;
    private GridSearchView searchView;
    private Class<T> type;
    private final String[] skipProperties;
    //
    private PagingGetTask fetchTask;
    private GetTask countTask;
    private SearchTask searchTask;

    public GridView(Class<T> type, int pageSize, String...skipProperties) {
        addClassNames("content");
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        //
        this.type = type;
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
            //return new FetchItems(ui, this, repository, page, size, delay);
            return null; //FIXME
        } else {
            fetchTask.updatePageQuery(
                    new Property("limit",Integer.toString(size))
                    , new Property("page", Integer.toString(page)));
        }
        return fetchTask;
    }

    public void setFetchTask(PagingGetTask fetchTask) {
        this.fetchTask = fetchTask;
    }

    public GetTask getCountTask(UI ui) {
        if (countTask == null) {
            //return new FetchItemsCount(ui, footer, repository);
            return null; //FIXME
        }
        return countTask;
    }

    public void setCountTask(GetTask countTask) {
        this.countTask = countTask;
    }

    public Task getSearchTask(UI ui, SearchQuery query, int delay) {
        if (searchTask == null) {
            //return new SearchItems(ui, this, repository, query, delay);
            return null; //FIXME
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
        //Task task = getFetchTask(ui, 0, grid.getPageSize(), 100);
        Task task = getFetchTask(ui, footer.getPage(), footer.getPageSize(), 100);
        AppQueue.dispatchTask(task);
        //Dispatch async event to fetch count:
        //Task countTask = new FetchItemsCount(ui, footer, repository);
        Task countTask = getCountTask(ui);
        AppQueue.dispatchTask(countTask);
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
        AppQueue.dispatchTask(task); //Async-load and server-side push
    }

    @Override
    public void onPreviousArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount) {
        UI ui = event.getSource().getUI().orElse(null);
        //Task task = new FetchItems(ui, this, repository, page, pageSize, 0);
        Task task = getFetchTask(ui, page, pageSize, 0);
        AppQueue.dispatchTask(task); //Async-load and server-side push
    }

    @Override
    public void onSearchEvent(ClickEvent<Button> event, Property search) {
        if (search.getValue() == null || search.getValue().toString().isEmpty()) {
            LOG.log(Level.WARNING, "Search Text is Null or Empty!");
            return;
        }
        LOG.log(Level.INFO,"SearchClicked: " + search.getValue());
        if (!Entity.class.isAssignableFrom(type)) {
            LOG.log(Level.WARNING, "Bean-Class Type dose not confirm to <com.infoworks.entity.Entity>.");
            return;
        }
        List<Property> searchProps = getProperties((Class<Entity>)type, skipProperties);
        SecureSearchQuery query = Pagination.of(SecureSearchQuery.class, 0, grid.getPageSize(), SortOrder.DESC);
        //query.setPage(0);
        query.setAuthorization(AuthRepository.parseToken(event.getSource().getUI().orElse(null)));
        //Iterate Over Search-Properties:
        for (Property prop : searchProps) {
            query.getPredicate()
                    .or(prop.getKey()).isLike("%" + search.getValue().toString() + "%");
        }
        //
        UI ui = UI.getCurrent().getUI().orElse(null);
        //Task searchTask = new SearchItems(ui, this, repository, query, 0);
        Task searchTask = getSearchTask(ui, query, 0);
        AppQueue.dispatchTask(searchTask);
    }

    public <E extends Entity> List<Property> getProperties(Class<E> type, String...skipKeys) {
        E item = null;
        try {
            item = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (!Objects.nonNull(item)) return new ArrayList<>();
        List<Property> properties = new ArrayList<>();
        //
        Map<String, Object> propMap = item.marshalling(false);
        propMap.forEach((key, value) -> {
            if (Arrays.stream(skipKeys)
                    .noneMatch(val -> val.equalsIgnoreCase(key))) {
                properties.add(new Property(key, value));
            }
        });
        return properties;
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

    public GridSearchView getSearchView() {
        return searchView;
    }
}
