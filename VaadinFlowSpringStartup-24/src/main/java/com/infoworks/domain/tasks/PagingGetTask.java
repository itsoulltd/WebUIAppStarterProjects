package com.infoworks.domain.tasks;

import com.infoworks.objects.Message;
import com.infoworks.objects.Response;
import com.infoworks.orm.Property;
import com.infoworks.utils.rest.client.GetTask;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Consumer;

public class PagingGetTask extends GetTask {

    public PagingGetTask() {super();}

    public PagingGetTask(String baseUri, String requestUri, Consumer<Response> response) {
        super(baseUri, requestUri, new Property[0], response);
    }

    private Map<String, Object> paramsKeyMaps = new HashMap<>();

    public PagingGetTask(String baseUri, String requestUri, Property...params) {
        super(baseUri, requestUri);
        Arrays.stream(params).forEach(param -> paramsKeyMaps.put(param.getKey(), param.getValue()));
        updateRequestUriWithQueryParams(requestUri, params);
    }

    private void updateRequestUriWithQueryParams(String requestUri, Property[] params) {
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

    public void updatePageQuery(Property page, Property size) {
        if (paramsKeyMaps.containsKey(page.getKey())) {
            paramsKeyMaps.put(page.getKey(), page.getValue());
        }
        if (paramsKeyMaps.containsKey(size.getKey())) {
            paramsKeyMaps.put(size.getKey(), size.getValue());
        }
        List<Property> paramList = new ArrayList<>();
        paramsKeyMaps.forEach((key, value) -> paramList.add(new Property(key, value.toString())));
        updateRequestUriWithQueryParams(requestUri, paramList.toArray(new Property[0]));
    }

    private RestTemplate restTemplate;

    public RestTemplate getTemplate() {
        if (restTemplate == null) restTemplate = new RestTemplate();
        return restTemplate;
    }

    public void setTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (getParams().length > 0) super.execute(message);
        //If-OtherWise:
        RestTemplate template = getTemplate();
        Map<String, String> headers = createAuthHeader(getToken());
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach((key, value) -> httpHeaders.set(key, value));
        //
        try {
            ResponseEntity<String> response = template.exchange(getUri()
                    , HttpMethod.GET
                    , new HttpEntity<>(getBody(), httpHeaders)
                    , String.class);
            if (getResponseListener() != null)
                getResponseListener().accept(new Response().setStatus(200).setMessage(response.getBody()));
            return new Response()
                    .setStatus(200)
                    .setMessage(response.getBody());
        } catch (Exception e) {
            return new Response()
                    .setStatus(500)
                    .setError(e.getMessage());
        }
    }
}
