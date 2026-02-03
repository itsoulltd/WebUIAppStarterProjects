package com.infoworks.config;

import com.vaadin.flow.component.UI;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

public class RestTemplateConfig {

    public static String X_REST_TEMPLATE_BEAN = "X_Template_Bean_Key";

    public static RestTemplate getCachedTemplate() {
        Object old = UI.getCurrent().getSession().getAttribute(X_REST_TEMPLATE_BEAN);
        if (old == null) {
            RestTemplate template = getTemplate();
            UI.getCurrent().getSession().setAttribute(X_REST_TEMPLATE_BEAN, template);
            old = template;
        }
        return (RestTemplate) old;
    }

    public static RestTemplate getTemplate() {
        HttpClient client = defaultHttpClient5();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate template = new RestTemplate(requestFactory);
        return template;
    }

    private static HttpClient defaultHttpClient5() {
        //Config connection pooling:
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);
        //Request config:
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(1200, TimeUnit.MILLISECONDS) // timeout to get connection from pool
                .setConnectTimeout(1000, TimeUnit.MILLISECONDS) // standard connection timeout
                .build();
        //Create http-client:
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

}
