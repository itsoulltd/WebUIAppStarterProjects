package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.component.CardView;
import com.infoworks.lab.components.component.FileDownload.FileDownload;
import com.infoworks.lab.components.component.FileUpload.FileUpload;
import com.infoworks.lab.config.ApplicationProperties;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.DisplayAsyncNotification;
import com.infoworks.lab.layouts.ApplicationLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.concurrent.TimeUnit;

@PageTitle("Profile")
@Route(value = RoutePath.PROFILE_VIEW, layout = ApplicationLayout.class)
public class ProfileView extends Composite<Div> {

    private String message = "Hello Vaadin EventQueue!";

    public ProfileView() {
        getContent().add(new Span("Profile"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getContent().getChildren().count() > 0){
            getContent().removeAll();
        }
        super.onAttach(attachEvent);
        //ProfileView Layout:
        VerticalLayout root = new VerticalLayout();
        //Cards:
        HorizontalLayout cardViewRow1 = new HorizontalLayout();
        cardViewRow1.setSpacing(false);
        //Revenue Card:
        CardView cardRevenue = new CardView("  Revenue  ", 732.09, 320.27);
        cardRevenue.setBorderAlignments(FlexComponent.Alignment.END, FlexComponent.Alignment.BASELINE);
        cardViewRow1.add(cardRevenue);
        //TotalOrder Card:
        CardView totalOrders = new CardView("  Total Orders  ", 531.0, 0.0);
        totalOrders.setBorderAlignments(FlexComponent.Alignment.END, FlexComponent.Alignment.BASELINE);
        cardViewRow1.add(totalOrders);
        //AssignedOrder Card:
        CardView assignedOrders = new CardView("  Assigned Orders  ", 231.0, 0.0);
        assignedOrders.setBorderAlignments(FlexComponent.Alignment.END, FlexComponent.Alignment.BASELINE);
        cardViewRow1.add(assignedOrders);
        //CompleteOrder Card:
        CardView completeOrders = new CardView("  Completed Orders  ", 101.0, 200.0);
        completeOrders.setBorderAlignments(FlexComponent.Alignment.END, FlexComponent.Alignment.BASELINE);
        cardViewRow1.add(completeOrders);
        //TransferredOrder Card:
        CardView transferOrders = new CardView("  Transferred Orders  ", 68.0, 75.0);
        transferOrders.setBorderAlignments(FlexComponent.Alignment.BASELINE);
        cardViewRow1.add(transferOrders);
        //Download View:
        FileDownload downloadView_1 = new FileDownload("Download Sample (*.xlsx): "
                , VaadinIcon.CLOUD_DOWNLOAD_O.create()
                , ApplicationProperties.SAMPLE_CREATE_ORDER_XLSX);
        FileDownload downloadView_2 = new FileDownload("Download Sample (*.xls): "
                , VaadinIcon.CLOUD_DOWNLOAD_O.create()
                , ApplicationProperties.SAMPLE_CREATE_ORDER_XLS);
        HorizontalLayout downloadGroup = new HorizontalLayout();
        downloadGroup.setWidthFull();
        downloadGroup.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        downloadGroup.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        downloadGroup.add(downloadView_1, downloadView_2);
        //Upload View:
        int maxFileSizeInMB = ApplicationProperties.APP_MAX_SIZE_IN_MB;
        FileUpload uploadView = new FileUpload();
        uploadView.setWidthFull();
        uploadView.setTitle(createTitle());
        uploadView.setTitleHint(createSubtitle(maxFileSizeInMB));
        uploadView.setUploadBtnTitle("Upload Batch File");
        uploadView.setUploadDropLabel(createDropLabel());
        uploadView.setAcceptedFileTypes("application/vnd.ms-excel"
                , "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                , ".xls"
                , ".xlsx");
        uploadView.setIncorrectFileTypeError("Please provide a Microsoft Excel (OpenXML) document.");
        uploadView.setMaxFileSizeInMB(maxFileSizeInMB);
        uploadView.setFileMaxSizeError("The file exceeds the maximum allowed size " + maxFileSizeInMB + "MB.");
        uploadView.setListener((event, iso) -> {
            //TODO:
            System.out.println("FileName: " + event.getFileName());
            System.out.println("FileSize: " + event.getContentLength());
            System.out.println("FileType: " + event.getMIMEType());
        });
        //Add to view:
        root.add(cardViewRow1, downloadGroup, uploadView);
        //Add Messaging Round-Trip view:
        Component messaging = createMessagingComponent();
        root.add(messaging);
        //Finally add the root layout to composite:
        getContent().add(root);
        //Now dispatch Rest-Api Calls:
        UI ui = UI.getCurrent();
        EventQueue.dispatch(700, TimeUnit.MILLISECONDS
                , () -> ui.access(() -> {
                    //Update Revenue Card View: UI
                    cardRevenue.update("$ %.2f", 980.87, 732.09);
                }));
        //
    }

    private H4 createTitle() {
        H4 title = new H4("Upload file to create order in batch");
        title.getStyle().set("margin-top", "0");
        return title;
    }

    private Paragraph createSubtitle(int maxFileSizeInMB) {
        Paragraph titleHint = new Paragraph("Accepted file formats: Microsoft Excel (.xls/.xlsx)"
                + " and maximum allowed size of " + maxFileSizeInMB + "MB.");
        titleHint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        return titleHint;
    }

    private Span createDropLabel() {
        Span cloudHint = new Span("Files will be uploaded to our cloud. Please note our ");
        Anchor policyLink = new Anchor(
                "https://vaadin.com/privacy-policy"
                , "privacy policy"
                , AnchorTarget.BLANK
        );
        return new Span(cloudHint, policyLink);
    }

    private Component createMessagingComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        //layout.setPadding(true);
        layout.setSpacing(true);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        //
        TextField messageLabel = new TextField("", "Enter Message!");
        messageLabel.addValueChangeListener((event) -> message = event.getValue());
        layout.add(messageLabel);
        //
        layout.add(new Button("Async Event", (event) -> {
            //Example How to do async ui update in Vaadin:
            UI ui = event.getSource().getUI().orElse(null);
            EventQueue.dispatchTask(new DisplayAsyncNotification(ui, message));
            //EventQueue.dispatchTaskInQueue(new DisplayAsyncNotification(ui, message));
        }));
        return layout;
    }
}