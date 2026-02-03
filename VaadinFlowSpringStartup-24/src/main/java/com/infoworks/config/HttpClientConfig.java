package com.infoworks.config;

import com.vaadin.flow.component.UI;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientConfig {

    public static String X_DEFAULT_HTTP_CLIENT_BEAN = "X_Http_Client_Bean_Key";

    public static HttpClient defaultHttpClient() {
        //Manage Per-Session:
        Object old = UI.getCurrent().getSession().getAttribute(X_DEFAULT_HTTP_CLIENT_BEAN);
        if (old == null) {
            HttpClient.Builder builder = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofMillis(700));
            /*builder = (getSecurity() != null) ? builder.sslContext(getSecurity()) : builder;
            builder = (getSslParameters() != null) ? builder.sslParameters(getSslParameters()) : builder;
            builder = (getAuthenticator() != null) ? builder.authenticator(getAuthenticator()) : builder ;
            builder = (getVersion() != null) ? builder.version(getVersion()) : builder;*/
            HttpClient _client = builder.build();
            UI.getCurrent().getSession().setAttribute(X_DEFAULT_HTTP_CLIENT_BEAN, _client);
            old = _client;
        }
        return (HttpClient) old;
    }

}
