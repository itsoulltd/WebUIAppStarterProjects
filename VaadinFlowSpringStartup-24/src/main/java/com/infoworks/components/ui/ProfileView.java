package com.infoworks.components.ui;

import com.infoworks.components.component.CardView;
import com.infoworks.components.component.FileDownload.FileDownload;
import com.infoworks.components.component.FileDownload.ImageDownload;
import com.infoworks.components.component.FileUpload.FileUpload;
import com.infoworks.components.component.FormActionBar;
import com.infoworks.config.AppQueue;
import com.infoworks.config.ApplicationProperties;
import com.infoworks.applayouts.RootLayout;
import com.infoworks.applayouts.RoutePath;
import com.infoworks.domain.tasks.DisplayAsyncNotification;
import com.infoworks.orm.Row;
import com.infoworks.services.excel.AsyncWriter;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@PageTitle("Profile")
@Route(value = RoutePath.PROFILE_VIEW, layout = RootLayout.class)
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
        //Set UploadView Title:
        H4 title = new H4("Upload file to create order in batch");
        title.getStyle().set("margin-top", "0");
        uploadView.setTitle(title);
        //Set UploadView TitleHint:
        Paragraph titleHint = new Paragraph("Accepted file formats: Microsoft Excel (.xls/.xlsx)"
                + " and maximum allowed size of " + maxFileSizeInMB + "MB.");
        titleHint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        uploadView.setTitleHint(titleHint);
        //Set UploadView Privacy-Policy:
        Span cloudHint = new Span("Files will be uploaded to our cloud. Please note our ");
        Anchor policyLink = new Anchor(
                "https://vaadin.com/privacy-policy"
                , "privacy policy"
                , AnchorTarget.BLANK
        );
        uploadView.setUploadDropLabel(new Span(cloudHint, policyLink));
        //
        uploadView.setUploadBtnTitle("Upload Batch File");
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
            System.out.println("FileSize: " + event.getFileSize());
            System.out.println("FileType: " + event.getContentType());
        });
        //Add to view:
        root.add(cardViewRow1, downloadGroup, uploadView);
        //Add Messaging Round-Trip view:
        Component messaging = createMessagingComponent();
        root.add(messaging);
        //Add ImageView with Download and Display:
        Component imageLoaderView = createImageLoaderViewComponent();
        root.add(imageLoaderView);
        //Add dynamic-report download view:
        Component dynamicDownloadView = createReportDownloadView();
        root.add(dynamicDownloadView);
        //Finally add the root layout to composite:
        getContent().add(root);
        //Now dispatch Rest-Api Calls:
        UI ui = UI.getCurrent();
        AppQueue.dispatch(700, TimeUnit.MILLISECONDS
                , () -> ui.access(() -> {
                    //Update Revenue Card View: UI
                    cardRevenue.update("$ %.2f", 980.87, 732.09);
                }));
        //
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
            AppQueue.dispatchTask(new DisplayAsyncNotification(ui, message));
            //EventQueue.dispatchTaskInQueue(new DisplayAsyncNotification(ui, message));
        }));
        return layout;
    }

    private String imageUrl = "";

    private Component createImageLoaderViewComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        //layout.setPadding(true);
        layout.setSpacing(true);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        //
        TextField urlLabel = new TextField("", "Enter Image Url!");
        urlLabel.setWidth("500px");
        urlLabel.addValueChangeListener((event) -> this.imageUrl = event.getValue());
        layout.add(urlLabel);
        //
        layout.add(new Button("Display Image", (event) -> {
            Dialog dialog = new Dialog();
            //ImageDownload imgView = new ImageDownload( "img/about/team1.jpg"); //Load static image from webapp/*
            //ImageDownload imgView = new ImageDownload( this.imageUrl); //Load from remote-url
            ImageDownload imgView = new ImageDownload( this.imageUrl, ""); //Load from remote-url and dynamically render to view.
            imgView.setDelaysInMillis(0);
            imgView.setLoadingMessage("Please wait...loading");
            //imgView.setAuthToken(AuthRepository.parseToken(UI.getCurrent()));
            dialog.add(imgView);
            dialog.open();
        }));
        return layout;
    }

    private Component createReportDownloadView() {
        HorizontalLayout layout = new HorizontalLayout();
        //layout.setPadding(true);
        layout.setSpacing(true);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        //Acknowledge Text:
        Text urlLabel = new Text("Example of dynamically generate excel report and download.");
        layout.add(urlLabel);

        //Prepare Data:
        String[] headers = {"AccountName","Currency","Amount","Balance","Type","Date","Ref"};
        String[] colKeys = {"account_ref","currency","amount","balance","transaction_type","transaction_date","transaction_ref"};
        Map<Integer, List<String>> data = new HashMap<>();
        List<Map> transactions = dummyTransactions();
        Map<Integer, List<String>> converted = AsyncWriter.convert(transactions, 1, colKeys);
        data.put(0, Arrays.asList(headers));
        data.putAll(converted);

        //AsyncWriter:
        try (AsyncWriter writer = new AsyncWriter(true, new ByteArrayOutputStream())) {
            writer.write("data", data, false);
            writer.flush();
            InputStream ios = new ByteArrayInputStream(((ByteArrayOutputStream) writer.getOutfile()).toByteArray());

            //Download action:
            Button download = new Button("Generate Report!", (event) -> {
                Dialog dialog = new Dialog();
                dialog.addDetachListener(e -> {
                    try { ios.close(); }
                    catch (IOException ex) { System.out.println(ex.getMessage()); }
                });
                //Prepare FileDownload:
                String reportName = String.format("Balance_Sheet_%s.xlsx", Instant.now().toEpochMilli());
                FileDownload downloadView =
                        new FileDownload("Click here! -> ", VaadinIcon.CLOUD_DOWNLOAD_O.create(), reportName, ios);
                dialog.add(downloadView);
                //ActionBar
                FormActionBar actionBar = new FormActionBar(dialog);
                actionBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
                actionBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
                dialog.add(actionBar);
                //
                dialog.open();
            });
            layout.add(download);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //
        return layout;
    }

    private List<Map> dummyTransactions() {
        List<Map> data = new ArrayList<>();
        data.add(new Row().add("account_ref", "CASH@admin").add("currency", "BDT").add("amount", "-230.0").add("balance", "1219.9").add("transaction_type", "withdrawal").add("transaction_date", "2026-01-14T19:38:20.318").add("transaction_ref", "cc25a914-4a84-4849").keyObjectMap());
        data.add(new Row().add("account_ref", "CASH@admin").add("currency", "BDT").add("amount", "1290.0").add("balance", "1449.9").add("transaction_type", "deposit").add("transaction_date", "2026-01-14T19:37:20.313").add("transaction_ref", "dd54cecd-80a5-4386").keyObjectMap());
        data.add(new Row().add("account_ref", "CASH@admin").add("currency", "BDT").add("amount", "-340.8").add("balance", "879.1").add("transaction_type", "transfer").add("transaction_date", "2026-01-14T19:36:20.312").add("transaction_ref", "ab4c7d73-dc84-433e").keyObjectMap());
        data.add(new Row().add("account_ref", "CASH@admin").add("currency", "BDT").add("amount", "-120.0").add("balance", "759.1").add("transaction_type", "transfer").add("transaction_date", "2026-01-14T19:35:20.317").add("transaction_ref", "daac741d-0ea9-49bc").keyObjectMap());
        data.add(new Row().add("account_ref", "CASH@admin").add("currency", "BDT").add("amount", "-30.1").add("balance", "159.9").add("transaction_type", "transfer").add("transaction_date", "2026-01-14T19:34:20.319").add("transaction_ref", "1248051c-5126-4f80").keyObjectMap());
        return data;
    }
}