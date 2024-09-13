package com.infoworks.lab.domain.repository;

import com.infoworks.lab.client.spring.HttpTemplate;
import com.infoworks.lab.config.RequestURI;
import com.infoworks.lab.config.RestTemplateConfig;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.entities.Persistable;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.domain.models.SecureSearchQuery;
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
                    , new HttpEntity<>(null, new HttpHeaders())
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
            Response items = get(null, new QueryParam("page", page.toString()), new QueryParam("limit", limit.toString()));
            if ( UserSessionManagement.handleSessionExpireEvent(items)) {
                throw new HttpInvocationException("Unauthorized Access!");
            }
            if (items instanceof ResponseList){
                List<E> collection = ((ResponseList)items).getCollections();
                return collection;
            }
        } catch (HttpInvocationException e) {
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
            E response = (E) post(ent);
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
            E response = (E) put(ent);
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
        List<E> ent;
        try {
            if (searchQuery instanceof SecureSearchQuery) {
                if (((SecureSearchQuery) searchQuery).getAuthorization() == null) {
                    String token = getToken();
                    String withPrefixToken = HttpInteractor.authorizationValue(token);
                    ((SecureSearchQuery) searchQuery).setAuthorization(withPrefixToken);
                }
            }
            ent = secureSearch(searchQuery);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (HttpInvocationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ent;
    }

    protected List<E> secureSearch(SearchQuery query)
            throws IOException, HttpInvocationException {
        //Pass Authorization token in HttpHeaders as auth-token.
        HttpHeaders headers = new HttpHeaders();
        if (query instanceof SecureSearchQuery) {
            String withPrefixToken = ((SecureSearchQuery) query).getAuthorization();
            headers.add(HttpHeaders.AUTHORIZATION, withPrefixToken);
        }
        //
        String url = schema() + host() + ":" + port() + api() + "/search";
        ResponseEntity<String> response = getTemplate().exchange(url
                , HttpMethod.POST
                , new HttpEntity<>(query, headers)
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
