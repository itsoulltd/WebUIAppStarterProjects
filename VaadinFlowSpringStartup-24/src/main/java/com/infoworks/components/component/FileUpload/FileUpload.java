package com.infoworks.components.component.FileUpload;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadEvent;

import java.io.InputStream;

public class FileUpload extends Div {

    private FileUploadViewListener listener;
    private H4 title;
    private Paragraph titleHint;
    private String uploadBtnTitle = "Upload Files";
    private String incorrectFileTypeError = "";
    private String fileMaxSizeError = "";
    private String uploadDropTitle = "Drop files here...";
    private Icon uploadDropIcon = VaadinIcon.CLOUD_UPLOAD_O.create();
    private Span uploadDropLabel;
    private int maxFileSizeInMB = 1;
    private String[] acceptedFileTypes = new String[]{};

    public FileUpload(FileUploadViewListener listener) {
        this.listener = listener;
    }

    public FileUpload() {this(null);}

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //
        //Accepted from Vaadin (24.8+): create with upload-success handler
        Upload upload = new Upload((uploadEvent) -> {
            try (InputStream ios = uploadEvent.getInputStream()) {
                if (listener != null) {
                    listener.onSuccess(uploadEvent, ios);
                }
            } catch (Exception e) { e.printStackTrace(); }
        });
        //Rejection handler:
        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show(
                    errorMessage,
                    5000,
                    Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            if (listener != null)
                listener.onRejected(event);
        });
        //Config:
        upload.setAutoUpload(false);
        upload.setDropAllowed(true);
        //Adding File-Type Restriction:
        if(acceptedFileTypes.length > 0)
            upload.setAcceptedFileTypes(acceptedFileTypes);
        //Adding File-Size Restriction:
        int maxFileSizeInBytes = 1024 * 1024 * maxFileSizeInMB;
        upload.setMaxFileSize(maxFileSizeInBytes);
        //
        UploadExamplesI18N i18n = new UploadExamplesI18N();
        i18n.getAddFiles().setOne(this.uploadBtnTitle);
        i18n.getDropFiles().setOne(this.uploadDropTitle);
        i18n.getError()
                .setIncorrectFileType(incorrectFileTypeError)
                .setFileIsTooBig(fileMaxSizeError);
        upload.setI18n(i18n);
        //Titles & subtitles:
        if (title == null) {
            title = new H4("Upload batch file");
            title.getStyle().set("margin-top", "0");
        }
        if (titleHint == null) {
            titleHint = new Paragraph("Accepted maximum allowed size of " + maxFileSizeInMB + "MB.");
            titleHint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        }
        //Custom drop-label & icon:
        if(uploadDropIcon != null) upload.setDropLabelIcon(uploadDropIcon);
        if(uploadDropLabel != null) upload.setDropLabel(uploadDropLabel);
        //
        add(title, titleHint, upload);
    }

    public void setUploadDropLabel(Span uploadDropLabel) {
        this.uploadDropLabel = uploadDropLabel;
    }

    public void setUploadDropIcon(Icon uploadDropIcon) {
        this.uploadDropIcon = uploadDropIcon;
    }

    public void setUploadBtnTitle(String uploadBtnTitle) {
        this.uploadBtnTitle = uploadBtnTitle;
    }

    public void setUploadDropTitle(String uploadDropTitle) {
        this.uploadDropTitle = uploadDropTitle;
    }

    public void setTitle(H4 title) {
        this.title = title;
    }

    public void setTitleHint(Paragraph titleHint) {
        this.titleHint = titleHint;
    }

    public void setIncorrectFileTypeError(String incorrectFileTypeError) {
        this.incorrectFileTypeError = incorrectFileTypeError;
    }

    public void setFileMaxSizeError(String fileMaxSizeError) {
        this.fileMaxSizeError = fileMaxSizeError;
    }

    public void setMaxFileSizeInMB(int maxFileSizeInMB) {
        this.maxFileSizeInMB = maxFileSizeInMB;
    }

    public void setAcceptedFileTypes(String...acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }

    public void setListener(FileUploadViewListener listener) {
        this.listener = listener;
    }

    @FunctionalInterface
    public interface FileUploadViewListener {
        void onSuccess(UploadEvent event, InputStream ios);
        default void onRejected(FileRejectedEvent event) {};
    }
}
