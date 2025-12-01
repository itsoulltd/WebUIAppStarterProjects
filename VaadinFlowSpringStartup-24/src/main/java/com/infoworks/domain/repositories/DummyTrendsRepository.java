package com.infoworks.domain.repositories;

import com.infoworks.domain.entities.Trend;
import com.infoworks.domain.models.ItemCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DummyTrendsRepository {

    protected String api() {
        return null;
    }

    public String getPrimaryKeyName() {
        return null;
    }

    public Class<Trend> getEntityType() {
        return null;
    }

    protected List<Trend> unmarshal(String json) throws IOException {
        return null;
    }

    public List<Trend> fetchDummyTrends(Integer page, Integer limit) {
        //return super.fetch(page, limit);
        //FIXME: For Testing Hardcoded value:
        List<Trend> trends = new ArrayList<>();
        trends.add(new Trend(1,"Facebook","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(2,"Twitter","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(3,"Instagram","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(4,"X-Handle","Almost 20 years old","Let's not be critical!"));
        return trends;
    }

    public ItemCount rowCount() {
        //return super.rowCount();
        //FIXME: For Testing Hardcoded value:
        ItemCount count = new ItemCount();
        count.setCount(4l);
        return count;
    }
}
