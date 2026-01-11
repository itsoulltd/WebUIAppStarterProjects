package com.infoworks.domain.repositories;

import com.infoworks.components.presenters.GridView.GridFooter;
import com.infoworks.components.presenters.GridView.GridView;
import com.infoworks.config.RequestURI;
import com.infoworks.domain.entities.Trend;
import com.infoworks.domain.models.ItemCount;
import com.infoworks.domain.tasks.PagingGetTask;
import com.infoworks.domain.tasks.SearchTask;
import com.infoworks.orm.Property;
import com.infoworks.utils.rest.client.GetTask;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
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
        trends.add(new Trend(1,"Facebook","40 years old","Let's not be critical!", true));
        trends.add(new Trend(2,"Twitter","Almost 35 years old","Kind of critical!", true));
        trends.add(new Trend(3,"Instagram","61 years old","Quite normal!"));
        trends.add(new Trend(4,"X","Almost 20 years old","In between critical!", true));
        return trends;
    }

    public ItemCount rowCount() {
        //return super.rowCount();
        //FIXME: For Testing Hardcoded value:
        ItemCount count = new ItemCount();
        count.setCount(4l);
        return count;
    }

    public PagingGetTask createPagingTask(UI ui, GridView gridView) {
        //FIXME:
        PagingGetTask pagingTask = new PagingGetTask(RequestURI.APP_BASE, "/api/trends/v1"
                , new Property("limit","10")
                , new Property("page", "0"));
        pagingTask.setBody(new HashMap<>(), AuthRepository.parseToken(ui));
        //
        Grid grid = gridView.getGrid();
        pagingTask.addResponseListener((response) -> {
            try{
                //FIXME:
                //List<Trend> trends = MessageParser.unmarshal(new TypeReference<>() {}, response.getMessage());
                List<Trend> trends = fetchDummyTrends(0, 10);
                ui.access(() -> grid.setItems(trends));
            } catch (Exception e) {}
        });
        //
        return pagingTask;
    }

    public GetTask createCountTask(UI ui, GridView gridView) {
        //FIXME:
        GetTask countTask = new GetTask(RequestURI.APP_BASE, "/api/trends/v1/rowCount");
        countTask.setBody(new HashMap<>(), AuthRepository.parseToken(ui));
        GridFooter footer = gridView.getFooter();
        countTask.addResponseListener((response) -> {
            try {
                //FIXME:
                //ItemCount count = MessageParser.unmarshal(ItemCount.class, response.getMessage());
                ItemCount count = rowCount();
                ui.access(() -> footer.updateTitleCount(count));
            } catch (Exception e) {}
        });
        return countTask;
    }

    public SearchTask createSearchTask(UI ui, GridView gridView) {
        //FIXME:
        SearchTask searchTask = new SearchTask(RequestURI.APP_BASE, "/api/trends/v1/search", null);
        searchTask.setBody(new HashMap<>(), AuthRepository.parseToken(ui));
        Grid grid = gridView.getGrid();
        searchTask.addResponseListener((response) -> {
            try{
                //FIXME:
                //List<Trend> trends = MessageParser.unmarshal(new TypeReference<>() {}, response.getMessage());
                List<Trend> trends = fetchDummyTrends(0, 10);
                ui.access(() -> grid.setItems(trends));
            } catch (Exception e) {}
        });
        return searchTask;
    }
}
