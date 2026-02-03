package com.infoworks.domain.tasks;

import com.infoworks.orm.Property;
import com.infoworks.sql.query.pagination.SearchQuery;
import com.infoworks.utils.rest.client.PostTask;

import java.util.Map;

public class SearchTask extends PostTask {

    public SearchTask() {super();}

    private Property fixedQuery;
    private SearchQuery updated;

    public SearchTask(String baseUri, String requestUri, Property fixedQuery) {
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
    public Map<String, Object> getBody() {
        SearchQuery cQuery = this.updated != null ? this.updated : new SearchQuery();
        return cQuery.marshalling(true);
    }
}
