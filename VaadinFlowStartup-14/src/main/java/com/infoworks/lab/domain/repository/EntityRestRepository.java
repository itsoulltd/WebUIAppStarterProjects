package com.infoworks.lab.domain.repository;

import com.infoworks.lab.client.jersey.HttpTemplate;
import com.infoworks.lab.config.RequestURI;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.entities.Persistable;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.domain.models.SecureSearchQuery;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.rest.models.*;
import com.infoworks.lab.rest.repository.RestRepository;
import com.infoworks.lab.rest.template.Invocation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityRestRepository<E extends Persistable, ID> extends HttpTemplate<E, Message> implements RestRepository<E, ID> {

    public EntityRestRepository(Object... config) {
        super(config);
    }

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    protected String schema() {
        return RequestURI.SCHEMA_HTTP;
    }

    @Override
    protected String host() {
        return RequestURI.APP_HOST;
    }

    @Override
    protected Integer port() {
        return Integer.valueOf(RequestURI.APP_PORT);
    }

    protected abstract String api();

    public abstract String getPrimaryKeyName();

    public abstract Class<E> getEntityType();

    public ItemCount rowCount() {
        try {
            javax.ws.rs.core.Response response = execute(null, Invocation.Method.GET, "rowCount");
            ItemCount iCount = inflate(response, ItemCount.class);
            return iCount;
        } catch (HttpInvocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ItemCount();
    }

    public List<E> fetch(Integer page, Integer limit){
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

    public E insert(E ent){
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
            e.printStackTrace();
        }
        return null;
    }

    public E update(E ent, ID id){
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
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(ID id){
        String token = getToken();
        return delete(id, token);
    }

    public boolean delete(ID id, String token){
        try {
            Authorization authorization = new Authorization(token);
            boolean isDeleted = delete(authorization, new QueryParam(getPrimaryKeyName(), id.toString()));
            return isDeleted;
        } catch (HttpInvocationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<E> search(SearchQuery searchQuery) {
        List<E> ent;
        try {
            if (searchQuery instanceof SecureSearchQuery) {
                if (((SecureSearchQuery) searchQuery).getAuthorization() == null) {
                    String token = getToken();
                    ((SecureSearchQuery) searchQuery).setAuthorization(token);
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
        javax.ws.rs.core.Response response = execute(query
                , Invocation.Method.POST
                , "search");
        if (UserSessionManagement.handleSessionExpireEvent(
                new Response().setStatus(response.getStatus()))) {
            throw new HttpInvocationException("Unauthorized Access!");
        }
        String responseStr = response.readEntity(String.class);
        List<E> ent = unmarshal(responseStr);
        return ent;
    }

    protected abstract List<E> unmarshal(String json) throws IOException;

}
