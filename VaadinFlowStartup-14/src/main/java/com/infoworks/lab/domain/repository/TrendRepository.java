package com.infoworks.lab.domain.repository;

import com.infoworks.lab.domain.entities.Trend;
import com.infoworks.lab.rest.models.ItemCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrendRepository extends EntityRestRepository<Trend, Integer> {

    @Override
    protected String api() {
        return null;
    }

    @Override
    public String getPrimaryKeyName() {
        return null;
    }

    @Override
    public Class<Trend> getEntityType() {
        return null;
    }

    @Override
    protected List<Trend> unmarshal(String json) throws IOException {
        return null;
    }

    @Override
    public List<Trend> fetch(Integer page, Integer limit) {
        //return super.fetch(page, limit);
        //FIXME: For Testing Hardcoded value:
        List<Trend> trends = new ArrayList<>();
        trends.add(new Trend(1,"Facebook","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(2,"Twitter","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(3,"Instagram","Almost 20 years old","Let's not be critical!"));
        trends.add(new Trend(4,"X-Handle","Almost 20 years old","Let's not be critical!"));
        return trends;
    }

    @Override
    public ItemCount rowCount() {
        //return super.rowCount();
        //FIXME: For Testing Hardcoded value:
        ItemCount count = new ItemCount();
        count.setCount(4l);
        return count;
    }
}
