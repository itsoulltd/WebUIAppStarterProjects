package com.infoworks.config;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientConfig {

    private static HttpClient _client;

    public static HttpClient defaultHttpClient() {
        //TODO: Manage Per-Session:
        if (_client == null) {
            HttpClient.Builder builder = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofMillis(700));
            /*builder = (getSecurity() != null) ? builder.sslContext(getSecurity()) : builder;
            builder = (getSslParameters() != null) ? builder.sslParameters(getSslParameters()) : builder;
            builder = (getAuthenticator() != null) ? builder.authenticator(getAuthenticator()) : builder ;
            builder = (getVersion() != null) ? builder.version(getVersion()) : builder;*/
            _client = builder.build();
        }
        return _client;
    }

}
