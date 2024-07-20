package com.infoworks.lab.components.presenters.Forms;

import com.infoworks.lab.components.component.FormActionBar;
import com.infoworks.lab.config.ValidationConfig;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.DisplayAsyncNotification;
import com.infoworks.lab.domain.entities.Trend;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextField;

import javax.validation.Validator;

public class TrendsForm extends FormLayout {

    private Trend trend;
    private Dialog dialog;
    private TextField title = new TextField("Title");
    private TextField subtitle = new TextField("Subtitle");
    private TextField description = new TextField("Descriptions");
    private TextField email = new TextField("Email");
    private FormActionBar actionBar;

    public TrendsForm(Trend trend, Dialog dialog) {
        this.trend = trend;
        this.dialog = dialog;
        this.actionBar = new FormActionBar(dialog);
    }

    public TrendsForm(Trend trend) {
        this.trend = trend;
        this.actionBar = new FormActionBar();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //TODO: UI need to be User-Entity:
        //
        this.actionBar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
        this.actionBar.setJustifyContentMode(JustifyContentMode.END);
        if (this.dialog != null) {
            //This will enableSave button:
            this.actionBar.setSaveButtonDisableOnClick(true);
            this.actionBar.addOnSaveAction((e) -> {
                UI ui = e.getSource().getUI().orElse(null);
                Validator validator = ValidationConfig.getValidator();
                String messages = ValidationConfig.validateWithMessage(validator, trend);
                if (messages != null) {
                    Notification notification = Notification.show(messages);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    this.actionBar.setSaveButtonEnable(true);
                } else {
                    //TODO: Now save entity:
                    EventQueue.dispatchTask(new DisplayAsyncNotification(ui
                            , "Save New Trends:" + trend.getTitle()));
                    dialog.close();
                }
            });
            //
            if (this.trend == null) {
                this.trend = new Trend();
                add(title, subtitle, description
                        , email, actionBar);
            } else {
                add(title, subtitle, description
                        , email, actionBar);
            }
        } else {
            //Make all field un-editable:
            title.setValue(trend.getTitle());
            subtitle.setValue(trend.getSubtitle());
            description.setValue(trend.getDescription());
            email.setValue(trend.getEmail());
            add(title, subtitle, description, email);
        }
        //UI config:
        setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("300px", 2)
        );
        // Stretch the username & actionBar field over 2 columns
        setColspan(description, 2);
        setColspan(actionBar, 2);
    }
}
