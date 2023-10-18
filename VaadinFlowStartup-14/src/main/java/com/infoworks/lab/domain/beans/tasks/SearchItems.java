package com.infoworks.lab.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.components.presenters.GridView.GridView;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.repository.RestRepository;
import com.vaadin.flow.component.UI;

import java.util.List;

public class SearchItems<T> extends ExecutableTask<Message, Response> {

    private UI ui;
    private GridView<T> grid;
    private RestRepository repository;
    private SearchQuery query;
    private final int delay;

    public SearchItems(UI ui, GridView<T> grid, RestRepository repository, SearchQuery query) {
        this(ui, grid, repository, query, 1000); //default 1 sec delay
    }

    public SearchItems(UI ui, GridView<T> grid, RestRepository repository, SearchQuery query, int delay) {
        this.ui = ui;
        this.grid = grid;
        this.repository = repository;
        this.query = query;
        this.delay = delay;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (ui != null) {
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {}
            }
            //
            try {
                List<T> res = repository.search(query);
                ui.access(() -> grid.setItems(res));
                System.out.println("Search Item count: " + res.size());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return new Response().setStatus(200);
    }

}
