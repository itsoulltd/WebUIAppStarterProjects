package com.infoworks.lab.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.components.presenters.GridView.GridView;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.repository.RestRepository;
import com.vaadin.flow.component.UI;

import java.util.List;

public class FetchItems<T> extends ExecutableTask<Message, Response> {

    private UI ui;
    private GridView<T> grid;
    private RestRepository repository;
    private int page;
    private int pageSize;
    private final int delay;

    public FetchItems(UI ui, GridView<T> grid, RestRepository repository, int page, int pageSize) {
        this(ui, grid, repository, page, pageSize, 1000); //default 1 sec delay
    }

    public FetchItems(UI ui, GridView<T> grid, RestRepository repository, int page, int pageSize, int delay) {
        this.ui = ui;
        this.grid = grid;
        this.repository = repository;
        this.page = page;
        this.pageSize = pageSize;
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
                List<T> res = repository.fetch(page, pageSize);
                ui.access(() -> grid.setItems(res));
                System.out.println("Items count: " + res.size());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return new Response().setStatus(200);
    }

}
