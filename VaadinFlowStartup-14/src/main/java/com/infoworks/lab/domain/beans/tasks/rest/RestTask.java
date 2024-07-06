package com.infoworks.lab.domain.beans.tasks.rest;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.entity.Entity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Consumer;

public abstract class RestTask<In extends Message, Out extends Response>
        extends ExecutableTask<In, Out> {

    protected String baseUri;
    protected String requestUri;
    protected String token;
    protected HttpEntity<Map> body;
    protected Object[] params = new Object[0];
    protected RestTemplate template;
    protected Consumer<String> responseListener;

    public RestTask(String baseUri, String requestUri, Consumer<String> responseListener) {
        this.baseUri = baseUri;
        this.requestUri = requestUri;
        this.responseListener = responseListener;
        this.template = new RestTemplate();
    }

    public RestTask(String baseUri, String requestUri) {
        this(baseUri, requestUri, null);
    }

    public RestTask() {
        this("", "", null);
    }

    public RestTask setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RestTask setRequestUri(String requestUri) {
        this.requestUri = requestUri;
        return this;
    }

    public RestTask setBody(Entity body, String token) {
        this.token = token == null ? "" : token;
        Map<String, Object> data = (body != null)
                ? body.marshallingToMap(true)
                : null;
        this.body = new HttpEntity(data, createHeaderFrom(this.token));
        return this;
    }

    protected HttpEntity<Map> getBody() {
        return this.body;
    }

    public RestTask setParams(Object...params) {
        this.params = params;
        return this;
    }

    protected Object[] getParams() {
        return this.params;
    }

    protected RestTemplate getTemplate() {
        return this.template;
    }

    public RestTask addResponseListener(Consumer<String> response) {
        this.responseListener = response;
        return this;
    }

    protected Consumer<String> getResponseListener() {
        return this.responseListener;
    }

    protected String getUri() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.baseUri.endsWith("/")
                ? this.baseUri.substring(0, (this.baseUri.length() - 1))
                : this.baseUri);
        builder.append(this.requestUri.startsWith("/")
                ? this.requestUri
                : "/" + this.requestUri);
        return builder.toString();
    }

    protected HttpHeaders createHeaderFrom(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (token.startsWith("Bearer")){
            httpHeaders.set(HttpHeaders.AUTHORIZATION, token);
        } else {
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return httpHeaders;
    }
}
