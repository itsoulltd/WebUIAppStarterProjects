package com.infoworks.lab.config;

import com.vaadin.flow.component.UI;
import org.apache.http.client.HttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfig {

    public static String X_REST_TEMPLATE_BEAN = "X_Template_Bean_Key";

    public static RestTemplate getTemplate() {
        HttpClient client = HttpClientConfig.defaultHttpClient();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        RestTemplate template = new RestTemplate(requestFactory);
        return template;
    }

    public static RestTemplate getCachedTemplate() {
        Object old = UI.getCurrent().getSession().getAttribute(X_REST_TEMPLATE_BEAN);
        if (old == null) {
            HttpClient client = HttpClientConfig.defaultHttpClient();
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
            RestTemplate template = new RestTemplate(requestFactory);
            UI.getCurrent().getSession().setAttribute(X_REST_TEMPLATE_BEAN, template);
            old = template;
        }
        return (RestTemplate) old;
    }

}
