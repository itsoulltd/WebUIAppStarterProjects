package com.infoworks.domain.tasks;

import com.infoworks.config.RequestURI;
import com.infoworks.config.RestTemplateConfig;
import com.infoworks.domain.models.OStreetGeocode;
import com.infoworks.objects.Message;
import com.infoworks.objects.Response;
import com.infoworks.sql.query.pagination.SearchQuery;
import com.infoworks.tasks.ExecutableTask;
import com.vaadin.flow.component.UI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class OStreetAddressSearch extends ExecutableTask<Message, Response> {

    private UI ui;
    private SearchQuery query;
    private Consumer<List<OStreetGeocode>> consumer;

    public OStreetAddressSearch(UI ui, String query, Consumer<List<OStreetGeocode>> consumer) {
        this.ui = ui;
        this.setQuery(query);
        this.consumer = consumer;
    }

    public OStreetAddressSearch(UI ui, Consumer<List<OStreetGeocode>> consumer) {
        this(ui, "", consumer);
    }

    public void setQuery(String query) {
        this.query = new SearchQuery();
        this.query.add("query").isEqualTo(query);
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (consumer == null) return new Response().setStatus(500).setError("Consumer is null!");
        try {
            List<OStreetGeocode> res = search(query);
            if(ui != null)
                ui.access(() -> consumer.accept(res));
            else
                consumer.accept(res);
            return new Response().setStatus(200).setMessage("Search Item count: " + res.size());
        } catch (Exception e) {
            return new Response().setStatus(500).setError(e.getMessage());
        }
    }

    public List<OStreetGeocode> search(SearchQuery searchQuery) {
        String query = Optional.ofNullable(searchQuery.get("query", String.class)).orElse("");
        HttpEntity<Map> body = new HttpEntity<>(null, new HttpHeaders());
        String url = RequestURI.OPEN_STREET_SEARCH_API + "?format={format}&q={q}";
        ResponseEntity<List<OStreetGeocode>> response = getTemplate().exchange(url
                , HttpMethod.GET
                , body
                , new ParameterizedTypeReference<List<OStreetGeocode>>() {}
                , "json", query);
        return response.getBody();
    }

    private RestTemplate restTemplate;

    public RestTemplate getTemplate() {
        if (restTemplate == null) restTemplate = RestTemplateConfig.getCachedTemplate();
        return restTemplate;
    }

    public void setTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
