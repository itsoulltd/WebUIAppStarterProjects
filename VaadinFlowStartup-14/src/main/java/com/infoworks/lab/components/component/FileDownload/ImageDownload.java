package com.infoworks.lab.components.component.FileDownload;

import com.infoworks.lab.config.ApplicationResources;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.rest.DownloadTask;
import com.infoworks.lab.util.services.iResourceService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.server.StreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageDownload extends Div {

    private static Logger LOG = LoggerFactory.getLogger("ImageDownload");
    private String baseUri;
    private String requestUri;
    private DownloadTask task;
    private String alt = "Not Found!";
    private iResourceService resService = iResourceService.create();
    private String loadingMessage = "Loading Image...";
    private long delaysInMillis = 0;
    private String authToken;

    public ImageDownload(String baseUri) {
        this.baseUri = baseUri;
    }

    public ImageDownload(String baseUri, String requestUri, Object...params) {
        this.baseUri = baseUri;
        this.requestUri = requestUri;
        this.task = new DownloadTask(this.baseUri, this.requestUri, params);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //Start Loading:
        if (delaysInMillis > 0l) {
            ProgressBar bar = new ProgressBar();
            bar.setIndeterminate(true);
            Div label = new Div();
            label.setText(loadingMessage);
            add(label, bar);
            //Dispatch Image in background:
            UI ui = UI.getCurrent();
            EventQueue.dispatch(delaysInMillis, TimeUnit.MILLISECONDS
                    , () -> ui.access(() -> displayImage()));
        } else {
            displayImage();
        }
    }

    protected void displayImage() {
        try {
            Image img = createImage(UI.getCurrent().getUI().orElse(null));
            removeAll(); //remove existing progressBar and message:
            if (img != null) {
                img.setSizeFull();
                add(img);
            } else {
                add(new Span("DownloadTask or BaseUri not defined!"));
            }
        } catch (Exception e) {
            add(new Span(e.getLocalizedMessage()));
        }
    }

    private Image createImage(UI ui) throws Exception {
        if (ui == null) return null;
        Image img = null;
        if (task != null) {
            if(getAuthToken() != null) task.setBody(new HashMap<>(), getAuthToken());
            DownloadTask.ResourceResponse response = task.execute(null);
            if (response == null) throw new Exception("ResourceResponse was null. Task.execute(...) failed!");
            if (response.getStatus() != 200) throw new Exception(response.getError());
            if (response.getResource() != null) {
                try (InputStream iso = response.getResource().getInputStream()) {
                    String filename = response.getResource().getFilename();
                    img = createImage(ui, iso, filename);
                }
            }
        } else {
            img = new Image(baseUri, getAlt());
        }
        return img;
    }

    protected Image createImage(UI ui, InputStream iso, String filename) throws Exception {
        String format = ApplicationResources.getFileExtension(filename);
        BufferedImage img = resService.readAsImage(iso, TYPE_INT_RGB);
        byte[] bytes = resService.readImageAsBytes(img
                , (format.startsWith("png")) ? iResourceService.Format.PNG : iResourceService.Format.JPEG);
        //Create Image using dynamic content:
        StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(bytes));
        Image image = new Image(resource, getAlt());
        return image;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public DownloadTask getTask() {
        return task;
    }

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public long getDelaysInMillis() {
        return delaysInMillis;
    }

    public void setDelaysInMillis(long delaysInMillis) {
        this.delaysInMillis = delaysInMillis;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
