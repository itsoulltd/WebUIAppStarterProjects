package com.infoworks.lab.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.domain.models.OStreetGeocode;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.models.SearchQuery;
import com.infoworks.lab.rest.repository.RestRepository;
import com.vaadin.flow.component.UI;

import java.util.List;
import java.util.function.Consumer;

public class OStreetAddressSearch extends ExecutableTask<Message, Response> {

    private UI ui;
    private RestRepository repository;
    private SearchQuery query;
    private Consumer<List<OStreetGeocode>> consumer;

    public OStreetAddressSearch(UI ui, RestRepository repository, String query, Consumer<List<OStreetGeocode>> consumer) {
        this.ui = ui;
        this.repository = repository;
        this.setQuery(query);
        this.consumer = consumer;
    }

    public OStreetAddressSearch(UI ui, RestRepository repository, Consumer<List<OStreetGeocode>> consumer) {
        this(ui, repository, "", consumer);
    }

    public void setQuery(String query) {
        this.query = new SearchQuery();
        this.query.add("query").isEqualTo(query);
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (consumer == null) return new Response().setStatus(500).setError("Consumer is null!");
        try {
            List<OStreetGeocode> res = repository.search(query);
            if(ui != null)
                ui.access(() -> consumer.accept(res));
            else
                consumer.accept(res);
            return new Response().setStatus(200).setMessage("Search Item count: " + res.size());
        } catch (Exception e) {
            return new Response().setStatus(500).setError(e.getMessage());
        }
    }

}
