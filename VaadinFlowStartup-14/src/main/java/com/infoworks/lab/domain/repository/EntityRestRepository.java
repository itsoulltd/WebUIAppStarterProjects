package com.infoworks.lab.domain.repository;

import com.infoworks.lab.client.jersey.HttpTemplate;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.entities.Persistable;
import com.infoworks.lab.domain.models.Authorization;
import com.infoworks.lab.domain.models.SecureSearchQuery;
import com.infoworks.lab.exceptions.HttpInvocationException;
import com.infoworks.lab.rest.models.*;
import com.infoworks.lab.rest.repository.RestRepository;
import com.infoworks.lab.rest.template.Invocation;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static com.infoworks.lab.domain.repository.AuthRepository.X_AUTH_TOKEN;

public abstract class EntityRestRepository<E extends Persistable, ID> extends HttpTemplate<E, Message> implements RestRepository<E, ID> {

    public EntityRestRepository(Object... config) {
        super(config);
    }

    @Override
    protected String schema() {
        return "http://";
    }

    @Override
    protected String host() {
        String host = System.getenv("app.host");
        return host == null ? "localhost" : host;
    }

    @Override
    protected Integer port() {
        String portStr = System.getenv("app.port");
        return portStr == null ? 8080 : Integer.valueOf(portStr);
    }

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
                String token = UI.getCurrent().getSession().getAttribute(X_AUTH_TOKEN).toString();
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
                String token = UI.getCurrent().getSession().getAttribute(X_AUTH_TOKEN).toString();
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
        String token = UI.getCurrent().getSession().getAttribute(X_AUTH_TOKEN).toString();
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
                    String token = UI.getCurrent().getSession().getAttribute(X_AUTH_TOKEN).toString();
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
