package com.infoworks.lab.domain.repository;

import com.infoworks.lab.config.RequestURI;
import com.infoworks.lab.domain.models.OStreetGeocode;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.SearchQuery;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OStreetRepository extends EntityRestRepository<OStreetGeocode, String> {

    private RestTemplate template = new RestTemplate();

    public OStreetRepository() {
        super(OStreetGeocode.class, Message.class);
    }

    public List<OStreetGeocode> search(SearchQuery searchQuery) {
        String query = Optional.ofNullable(searchQuery.get("query", String.class)).orElse("");
        HttpEntity<Map> body = new HttpEntity<>(null, new HttpHeaders());
        String url = api() + "?format={format}&q={q}";
        ResponseEntity<List<OStreetGeocode>> response = template.exchange(url
                , HttpMethod.GET
                , body
                , new ParameterizedTypeReference<List<OStreetGeocode>>() {}
                , "json", query);
        return response.getBody();
    }

    @Override
    protected String api() {
        return RequestURI.OPEN_STREET_SEARCH_API;
    }

    @Override
    public String getPrimaryKeyName() {
        return "";
    }

    @Override
    public Class<OStreetGeocode> getEntityType() {
        return OStreetGeocode.class;
    }

    @Override
    protected List<OStreetGeocode> unmarshal(String json) throws IOException {
        return null;
    }
}
