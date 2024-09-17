package com.infoworks.lab.domain.repository;

import com.infoworks.lab.beans.tasks.rest.client.spring.methods.GetTask;
import com.infoworks.lab.client.spring.HttpTemplate;
import com.infoworks.lab.config.RequestURI;
import com.infoworks.lab.config.RestTemplateConfig;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.entities.Persistable;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.rest.models.*;
import com.infoworks.lab.rest.repository.RestRepository;
import com.infoworks.lab.rest.template.HttpInteractor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class EntitySpringRestRepository<E extends Persistable, ID> extends HttpTemplate<E, Message> implements RestRepository<E, ID> {

    private final RestTemplate template;

    public EntitySpringRestRepository(Object... config) {
        super(config);
        this.template = RestTemplateConfig.getTemplate();
    }

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        String onlyToken = HttpInteractor.parseToken(getToken());
        headers.set(HttpHeaders.AUTHORIZATION, HttpInteractor.authorizationValue(onlyToken));
        return headers;
    }

    public RestTemplate getTemplate() {
        return template;
    }

    @Override
    protected String schema() {
        return RequestURI.SCHEMA_HTTP;
    }

    protected abstract String api();

    public abstract String getPrimaryKeyName();

    public abstract Class<E> getEntityType();

    public ItemCount rowCount() {
        try {
            String url = schema() + host() + ":" + port() + api() + "/rowCount";
            ResponseEntity<String> response = getTemplate().exchange(url
                    , HttpMethod.GET
                    , new HttpEntity<>(null, getHeader())
                    , String.class);
            ItemCount iCount = Message.unmarshal(ItemCount.class, response.getBody());
            return iCount;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ItemCount();
    }

    public List<E> fetch(Integer page, Integer limit) {
        try {
            String url = schema() + host() + ":" + port() + api();
            GetTask task = new GetTask(url, "?page={page}&limit={limit}", page, limit);
            task.setBody(new HashMap<>(), getToken());
            task.setTemplate(RestTemplateConfig.getTemplate());
            Response response = task.execute(null);
            return unmarshal(response.getPayload());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public E insert(E ent) throws RuntimeException {
        try {
            if (ent.getAuthorization() == null) {
                String token = getToken();
                ent.setAuthorization(token);
            }
            E response = post(ent);
            if (UserSessionManagement.handleSessionExpireEvent(response)) {
                throw new HttpInvocationException("Unauthorized Access!");
            }
            return response;
        } catch (HttpInvocationException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public E update(E ent, ID id) throws RuntimeException {
        try {
            if (ent.getAuthorization() == null) {
                String token = getToken();
                ent.setAuthorization(token);
            }
            ent.setId(id);
            E response = put(ent);
            if (UserSessionManagement.handleSessionExpireEvent(response)) {
                throw new HttpInvocationException("Unauthorized Access!");
            }
            return response;
        } catch (HttpInvocationException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public boolean delete(ID id) throws RuntimeException {
        String token = getToken();
        return delete(id, token);
    }

    public boolean delete(ID id, String token) throws RuntimeException {
        try {
            Authorization authorization = new Authorization(token);
            boolean isDeleted = delete(authorization, new QueryParam(getPrimaryKeyName(), id.toString()));
            return isDeleted;
        } catch (HttpInvocationException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public List<E> search(SearchQuery searchQuery) {
        try {
            List<E> ent = secureSearch(searchQuery);
            return ent;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (HttpInvocationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<E> secureSearch(SearchQuery query)
            throws IOException, HttpInvocationException {
        String url = schema() + host() + ":" + port() + api() + "/search";
        ResponseEntity<String> response = getTemplate().exchange(url
                , HttpMethod.POST
                , new HttpEntity<>(query, getHeader())
                , String.class);
        if (UserSessionManagement.handleSessionExpireEvent(
                new Response().setStatus(response.getStatusCodeValue()))) {
            throw new HttpInvocationException("Unauthorized Access!");
        }
        String responseStr = response.getBody();
        List<E> ent = unmarshal(responseStr);
        return ent;
    }

    protected abstract List<E> unmarshal(String json) throws IOException;

}
