package com.infoworks.lab.domain.beans.tasks.rest;

import com.infoworks.lab.beans.tasks.rest.client.spring.methods.GetTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.QueryParam;
import com.infoworks.lab.rest.models.Response;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Consumer;

public class PagingGetTask extends GetTask {

    public PagingGetTask() {super();}

    public PagingGetTask(String baseUri, String requestUri, Object...params) {
        super(baseUri, requestUri, params);
    }

    public PagingGetTask(String baseUri, String requestUri, Consumer<String> response) {
        super(baseUri, requestUri, response);
    }

    private Map<String, Object> paramsKeyMaps = new HashMap<>();

    public PagingGetTask(String baseUri, String requestUri, QueryParam...params) {
        super(baseUri, requestUri);
        Arrays.stream(params).forEach(param -> paramsKeyMaps.put(param.getKey(), param.getValue()));
        updateRequestUriWithQueryParams(requestUri, params);
    }

    private void updateRequestUriWithQueryParams(String requestUri, QueryParam[] params) {
        //Update paths?<query-params>
        String queryParam = urlencodedQueryParam(params);
        requestUri = requestUri.trim();
        if (requestUri.contains("?")) {
            String paths = requestUri.substring(0, requestUri.indexOf("?"));
            setRequestUri(paths + queryParam);
        } else {
            setRequestUri(requestUri + queryParam);
        }
    }

    public void updatePageQuery(QueryParam page, QueryParam size) {
        if (paramsKeyMaps.containsKey(page.getKey())) {
            paramsKeyMaps.put(page.getKey(), page.getValue());
        }
        if (paramsKeyMaps.containsKey(size.getKey())) {
            paramsKeyMaps.put(size.getKey(), size.getValue());
        }
        List<QueryParam> paramList = new ArrayList<>();
        paramsKeyMaps.forEach((key, value) -> paramList.add(new QueryParam(key, value.toString())));
        updateRequestUriWithQueryParams(requestUri, paramList.toArray(new QueryParam[0]));
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (getParams().length > 0) super.execute(message);
        //If-OtherWise:
        RestTemplate template = getTemplate();
        try {
            ResponseEntity<String> response = template.exchange(getUri()
                    , HttpMethod.GET
                    , getBody()
                    , String.class);
            if (getResponseListener() != null)
                getResponseListener().accept(response.getBody());
            return (Response) new Response()
                    .setStatus(200)
                    .setMessage(getUri())
                    .setPayload(response.getBody());
        } catch (Exception e) {
            return new Response()
                    .setStatus(500)
                    .setMessage(getUri())
                    .setError(e.getMessage());
        }
    }
}
