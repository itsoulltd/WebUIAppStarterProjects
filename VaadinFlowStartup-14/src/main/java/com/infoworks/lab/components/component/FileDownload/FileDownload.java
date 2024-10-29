package com.infoworks.lab.components.component.FileDownload;

import com.infoworks.lab.config.ApplicationResources;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class FileDownload extends Div {

    private String title;
    private final Icon icon;
    private File resource;
    private Path resourcePath;

    public FileDownload(String title, Icon icon, File resource) {
        this.title = title;
        this.icon = (icon == null) ? VaadinIcon.CLOUD_DOWNLOAD_O.create() : icon;
        this.resource = resource;
    }

    public FileDownload(String title, Icon icon, Path resourcePath) {
        this(title, icon, new File(resourcePath.toFile().getAbsolutePath()));
        this.resourcePath = resourcePath;
    }

    public FileDownload(String title, Icon icon, String resourceName) {
        this(title, icon, new ApplicationResources().getPath(resourceName));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //Css:
        //getStyle().set("margin", "25px 50px 50px 25px"); //top-right-bottom-left
        getStyle().set("padding", "10px 10px 10px 10px"); //top-right-bottom-left
        getStyle().set("text-align", "center");
        //getStyle().set("vertical-align", "baseline");
        //getStyle().set("background-color", "#e6f3ff");
        //getStyle().set("border", "1px solid #c5c5c5");
        getStyle().set("border", "1px dashed #c5c5c5"); //dotted
        getStyle().set("border-radius", "var(--lumo-border-radius-m)"); //-s | -m | -l
        //Render UI:
        Anchor downloadLink = createDownloadLinkFrom(resource);
        Span titleWithLink = new Span(new Span(title), downloadLink);
        //titleWithLink.getStyle().set("margin-top", "30px");
        //Icon:
        icon.getStyle().set("margin-right", "5px");
        //Add all:
        add(icon, titleWithLink);
    }

    private Anchor createDownloadLinkFrom(File resource) {
        Optional<InputStream> ios = getInputStream(resource);
        StreamResource streamResource = new StreamResource(resource.getName(), () -> ios.orElse(null));
        Anchor link = new Anchor(streamResource
                , String.format("%s (%d KB)", resource.getName(),
                (int) resource.length() / 1024));
        link.getElement().setAttribute("download", true);
        return link;
    }

    private Optional<InputStream> getInputStream(File resource) {
        if (resource == null) return Optional.ofNullable(null);
        try {
            return Optional.ofNullable(new FileInputStream(resource));
        } catch (FileNotFoundException e) {}
        return Optional.ofNullable(null);
    }
}
