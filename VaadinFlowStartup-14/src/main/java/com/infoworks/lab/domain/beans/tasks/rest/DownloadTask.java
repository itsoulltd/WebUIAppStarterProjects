package com.infoworks.lab.domain.beans.tasks.rest;

import com.infoworks.lab.beans.tasks.rest.client.spring.methods.GetTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.util.services.iResourceService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.function.Consumer;

public class DownloadTask extends GetTask {

    public DownloadTask() {super();}

    public DownloadTask(String baseUri, String requestUri, Object...params) {
        super(baseUri, requestUri, params);
    }

    public DownloadTask(String baseUri, String requestUri, Consumer<String> response) {
        super(baseUri, requestUri, response);
    }

    @Override
    public ResourceResponse execute(Message message) throws RuntimeException {
        RestTemplate template = getTemplate();
        try {
            ResponseEntity<Resource> response = template.exchange(getUri()
                    , HttpMethod.GET
                    , getBody()
                    , Resource.class
                    , getParams());
            if (getResponseListener() != null) {
                String base64Encoded = null;
                if (response != null && response.getBody() != null) {
                    Resource resource = response.getBody();
                    iResourceService service = iResourceService.create();
                    byte[] bytes = service.readAsBytes(resource.getInputStream());
                    base64Encoded = new String(Base64.getEncoder().encode(bytes), "UTF-8");
                }
                getResponseListener().accept(base64Encoded);
            }
            return (ResourceResponse) new ResourceResponse()
                    .setResource(response.getBody())
                    .setStatus(200)
                    .setMessage(getUri());
        } catch (Exception e) {
            return (ResourceResponse) new ResourceResponse()
                    .setResource(null)
                    .setStatus(500)
                    .setMessage(getUri())
                    .setError(e.getMessage());
        }
    }

    protected String getUri() {
        StringBuilder builder = new StringBuilder();
        if (this.baseUri != null && !this.baseUri.isEmpty()) {
            builder.append(this.baseUri.endsWith("/")
                    ? this.baseUri.substring(0, (this.baseUri.length() - 1))
                    : this.baseUri);
        }
        if (this.requestUri != null && !this.requestUri.isEmpty()) {
            builder.append(this.requestUri.startsWith("/")
                    ? this.requestUri
                    : "/" + this.requestUri);
        }
        return builder.toString();
    }

    public static class ResourceResponse extends Response {
        private Resource resource;

        public Resource getResource() {
            return resource;
        }

        public ResourceResponse setResource(Resource resource) {
            this.resource = resource;
            return this;
        }
    }
}
