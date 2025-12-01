package com.infoworks.components.presenters.GridView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.config.AppQueue;
import com.infoworks.domain.models.ItemCount;
import com.infoworks.domain.models.SecureSearchQuery;
import com.infoworks.domain.repositories.AuthRepository;
import com.infoworks.domain.tasks.PagingGetTask;
import com.infoworks.domain.tasks.SearchTask;
import com.infoworks.entity.Entity;
import com.infoworks.objects.MessageParser;
import com.infoworks.orm.Property;
import com.infoworks.sql.query.pagination.Pagination;
import com.infoworks.sql.query.pagination.SearchQuery;
import com.infoworks.sql.query.pagination.SortOrder;
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
    private GetTask countTask;
    private PagingGetTask fetchTask;
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

    public Task getFetchTask(int page, int size, int delay) throws RuntimeException {
        if (fetchTask == null) {
            throw new RuntimeException("fetchTask<PagingGetTask> not been set! Please review the GridView integration flow!");
        } else {
            fetchTask.updatePageQuery(
                    new Property("limit",Integer.toString(size))
                    , new Property("page", Integer.toString(page)));
        }
        return fetchTask;
    }

    public void setFetchTask(UI ui, PagingGetTask fetchTask, boolean skipResponseListener, TypeReference<List<T>> typeRef) {
        this.fetchTask = fetchTask;
        if (!skipResponseListener) {
            this.fetchTask.addResponseListener((response) -> {
                try {
                    List<T> data = MessageParser.unmarshal(typeRef, response.getMessage());
                    ui.access(() -> grid.setItems(data));
                } catch (Exception e) {
                    LOG.log(Level.WARNING, e.getMessage());
                }
            });
        }
    }

    public GetTask getCountTask() throws RuntimeException {
        if (countTask == null) {
            throw new RuntimeException("countTask<GetTask> not been set! Please review the GridView integration flow!");
        }
        return countTask;
    }

    public void setCountTask(UI ui, GetTask countTask, boolean skipResponseListener) {
        this.countTask = countTask;
        if (!skipResponseListener) {
            this.countTask.addResponseListener((response) -> {
                try {
                    ItemCount count = MessageParser.unmarshal(ItemCount.class, response.getMessage());
                    ui.access(() -> footer.updateTitleCount(count));
                } catch (Exception e) {
                    LOG.log(Level.WARNING, e.getMessage());
                }
            });
        }
    }

    public Task getSearchTask(SearchQuery query, int delay) throws RuntimeException {
        if (searchTask == null) {
            throw new RuntimeException("searchTask<SearchTask> not been set! Please review the GridView integration flow!");
        } else {
            searchTask.updateQuery(query);
        }
        return searchTask;
    }

    public void setSearchTask(UI ui, SearchTask searchTask, boolean skipResponseListener, TypeReference<List<T>> typeRef) {
        this.searchTask = searchTask;
        if (!skipResponseListener) {
            this.searchTask.addResponseListener((response) -> {
                try {
                    List<T> data = MessageParser.unmarshal(typeRef, response.getMessage());
                    ui.access(() -> grid.setItems(data));
                } catch (Exception e) {
                    LOG.log(Level.WARNING, e.getMessage());
                }
            });
        }
    }

    public void dispatchAsyncLoad(UI ui) {
        if (ui == null) return;
        //Dispatch async event to fetch user-list:
        Task task = getFetchTask(footer.getPage(), footer.getPageSize(), 100);
        AppQueue.dispatchTask(task);
        //Dispatch async event to fetch count:
        Task countTask = getCountTask();
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
        Task task = getFetchTask(page, pageSize, 0);
        AppQueue.dispatchTask(task); //Async-load and server-side push
    }

    @Override
    public void onPreviousArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount) {
        UI ui = event.getSource().getUI().orElse(null);
        //Task task = new FetchItems(ui, this, repository, page, pageSize, 0);
        Task task = getFetchTask(page, pageSize, 0);
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
        Task searchTask = getSearchTask(query, 0);
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
