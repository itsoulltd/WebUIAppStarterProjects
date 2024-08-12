package com.infoworks.lab.domain.beans.tasks.rest;

import com.infoworks.lab.beans.tasks.rest.client.spring.methods.PostTask;
import com.infoworks.lab.rest.models.QueryParam;
import com.infoworks.lab.rest.models.SearchQuery;
import org.springframework.http.HttpEntity;

import java.util.function.Consumer;

public class SearchTask extends PostTask {

    public SearchTask() {super();}

    public SearchTask(String baseUri, String requestUri, Consumer<String> response) {
        super(baseUri, requestUri, response);
    }

    private QueryParam query;
    private SearchQuery updated;

    public SearchTask(String baseUri, String requestUri, QueryParam query) {
        super(baseUri, requestUri);
        this.query = query;
    }

    public void updateQuery(SearchQuery updated) {
        SearchQuery nQuery = new SearchQuery();
        nQuery.setPage(updated.getPage());
        nQuery.setSize(updated.getSize());
        //
        nQuery.add(query.getKey()).isEqualTo(query.getValue());
        updated.getProperties().forEach(prm -> nQuery.getPredicate().or(prm.getKey()).isLike(prm.getValue()));
        this.updated = nQuery;
    }

    @Override
    protected HttpEntity getBody() {
        SearchQuery cQuery = this.updated != null ? this.updated : new SearchQuery();
        HttpEntity entity = new HttpEntity(cQuery, createHeaderFrom(this.token));
        return entity;
    }
}
