package com.infoworks.domain.repositories;

import com.infoworks.components.presenters.GridView.GridFooter;
import com.infoworks.components.presenters.GridView.GridView;
import com.infoworks.config.HttpClientConfig;
import com.infoworks.config.RequestURI;
import com.infoworks.data.impl.SimpleDataSource;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DummyTrendsRepository extends SimpleDataSource<Integer, Trend> {

    public DummyTrendsRepository() {
        loadIntoMemory();
    }

    private void loadIntoMemory() {
        //For Testing Hardcoded value:
        int index = 0;
        List<Trend> trends = new ArrayList<>();
        trends.add(new Trend(++index,"Facebook","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"Twitter","Almost 35 years old","Kind of critical!", false));
        trends.add(new Trend(++index,"Instagram","61 years old","Quite normal!", true));
        trends.add(new Trend(++index,"X","Almost 20 years old","In between critical!", true));
        trends.add(new Trend(++index,"TikTok","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"Snapchat","Almost 35 years old","Kind of critical!", true));
        trends.add(new Trend(++index,"Threads","61 years old","Quite normal!", false));
        trends.add(new Trend(++index,"LinkedIn","Almost 20 years old","In between critical!", true));
        trends.add(new Trend(++index,"GitHub","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"GitLab","Almost 35 years old","Kind of critical!", false));
        trends.add(new Trend(++index,"Stack Overflow","61 years old","Quite normal!", true));
        trends.add(new Trend(++index,"YouTube","Almost 20 years old","In between critical!", true));
        trends.add(new Trend(++index,"Twitch","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"Kick","Almost 35 years old","Kind of critical!", false));
        trends.add(new Trend(++index,"Vimeo","61 years old","Quite normal!", false));
        trends.add(new Trend(++index,"Signal","Almost 20 years old","In between critical!", true));
        trends.add(new Trend(++index,"Telegram","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"Vimeo","Almost 35 years old","Kind of critical!", true));
        trends.add(new Trend(++index,"Messenger","61 years old","Quite normal!", false));
        trends.add(new Trend(++index,"Discord","Almost 20 years old","In between critical!", true));
        trends.add(new Trend(++index,"WeChat","40 years old","Let's not be critical!", true));
        trends.add(new Trend(++index,"Pinterest","Almost 35 years old","Kind of critical!", true));
        trends.add(new Trend(++index,"Substack","61 years old","Quite normal!", true));
        trends.add(new Trend(++index,"Tumblr","Almost 20 years old","In between critical!", true));
        //InsertInto:
        trends.forEach(trend -> put(trend.getId(), trend));
    }

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
        Object[] objs = readSync(page, limit);
        List<Trend> trends = Arrays.stream(objs).map(trend -> {
            Trend trd = (Trend) trend;
            return trd;
        }).toList();
        return trends;
    }

    public ItemCount rowCount() {
        //return super.rowCount();
        ItemCount count = new ItemCount();
        count.setCount(Integer.valueOf(size()).longValue());
        count.setStatus(200);
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
                Map<String, String> queryMap = parseQueryParam(response.getPayload());
                int page = Integer.valueOf(Optional.ofNullable(queryMap.get("page")).orElse("0"));
                int limit = Integer.valueOf(Optional.ofNullable(queryMap.get("limit")).orElse("10"));
                List<Trend> trends = fetchDummyTrends(page, limit);
                ui.access(() -> grid.setItems(trends));
            } catch (Exception e) {}
        });
        //
        return pagingTask;
    }

    private Map<String, String> parseQueryParam(String payload) {
        try {
            URL url = new URL(payload);
            Map<String, String> queryMap = Arrays.stream(url.getQuery().split("&"))
                    .map(p -> p.split("="))
                    .collect(Collectors.toMap(p -> p[0], p -> p[1]));
            return queryMap;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public GetTask createCountTask(UI ui, GridView gridView) {
        //FIXME:
        GetTask countTask = new GetTask(RequestURI.APP_BASE, "/api/trends/v1/rowCount");
        countTask.setBody(new HashMap<>(), AuthRepository.parseToken(ui));
        countTask.setClient(HttpClientConfig.defaultHttpClient());
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
                List<Trend> trends = fetchDummyTrends(1, 5);
                ui.access(() -> grid.setItems(trends));
            } catch (Exception e) {}
        });
        return searchTask;
    }
}
