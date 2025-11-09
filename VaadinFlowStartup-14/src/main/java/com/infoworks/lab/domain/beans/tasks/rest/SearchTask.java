package com.infoworks.lab.domain.beans.tasks.rest;

import com.infoworks.lab.beans.tasks.rest.client.spring.methods.PostTask;
import com.infoworks.lab.rest.models.QueryParam;
import com.infoworks.lab.rest.models.SearchQuery;
import org.springframework.http.HttpEntity;

public class SearchTask extends PostTask {

    public SearchTask() {super();}

    private QueryParam fixedQuery;
    private SearchQuery updated;

    public SearchTask(String baseUri, String requestUri, QueryParam fixedQuery) {
        super(baseUri, requestUri);
        this.fixedQuery = fixedQuery;
    }

    public void updateQuery(SearchQuery updated) {
        SearchQuery nQuery = new SearchQuery();
        nQuery.setPage(updated.getPage());
        nQuery.setSize(updated.getSize());
        //
        if(fixedQuery != null) nQuery.add(fixedQuery.getKey()).isEqualTo(fixedQuery.getValue());
        updated.getProperties().forEach(prm -> nQuery.getPredicate().or(prm.getKey()).isLike(prm.getValue()));
        this.updated = nQuery;
    }

    @Override
    public HttpEntity getBody() {
        SearchQuery cQuery = this.updated != null ? this.updated : new SearchQuery();
        HttpEntity entity = new HttpEntity(cQuery, createHeaderFrom(this.token));
        return entity;
    }
}
