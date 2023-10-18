package com.infoworks.lab.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.components.presenters.GridView.GridFooter;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.repository.RestRepository;
import com.vaadin.flow.component.UI;

public class FetchItemsCount extends ExecutableTask<Message, Response> {

    private UI ui;
    private RestRepository repository;
    private GridFooter footer;

    public FetchItemsCount(UI ui, GridFooter footer, RestRepository repository) {
        this.ui = ui;
        this.repository = repository;
        this.footer = footer;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (ui != null) {
            ItemCount count = repository.rowCount();
            ui.access(() -> footer.updateTitleCount(count));
        }
        return new Response().setStatus(200);
    }
}
