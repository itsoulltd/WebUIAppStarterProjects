package com.infoworks.components.presenters.Forms;

import com.infoworks.components.component.FormActionBar;
import com.infoworks.config.ValidationConfig;
import com.infoworks.config.AppQueue;
import com.infoworks.domain.tasks.DisplayAsyncNotification;
import com.infoworks.domain.entities.Trend;
import com.infoworks.domain.models.EventType;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.Validator;

import java.util.Optional;

public class TrendsForm<Entity extends Trend> extends FormLayout {
    private EventType eventType;
    private Entity entity;
    private Dialog dialog;
    private TextField title = new TextField("Title");
    private TextField subtitle = new TextField("Subtitle");
    private TextField email = new TextField("Email");
    private TextArea description = new TextArea("Descriptions");
    private FormActionBar actionBar;

    public TrendsForm(Entity entity, Dialog dialog) {
        this.entity = entity;
        this.dialog = dialog;
        this.eventType = (entity == null) ? EventType.CREATE : EventType.UPDATE;
        this.actionBar = new FormActionBar(dialog);
    }

    public TrendsForm(Entity entity) {
        this(entity, null);
    }

    private Entity newEntity() {
        return (Entity) new Trend();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //
        this.actionBar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
        this.actionBar.setJustifyContentMode(JustifyContentMode.END);
        if (this.dialog != null) {
            //This will enableSave button:
            this.actionBar.setSaveButtonDisableOnClick(true);
            this.actionBar.addOnSaveAction(onSaveAction());
            //If model is null:
            if (this.entity == null) {
                this.entity = newEntity();
            }
            //Update UI:
            if (eventType == EventType.UPDATE) {
                title.setValue(Optional.ofNullable(entity.getTitle()).orElse(""));
                subtitle.setValue(Optional.ofNullable(entity.getSubtitle()).orElse(""));
                email.setValue(Optional.ofNullable(entity.getEmail()).orElse(""));
                description.setValue(Optional.ofNullable(entity.getDescription()).orElse(""));
            } else {
                title.setRequired(true);
            }
            add(title, subtitle, email, description
                    , actionBar);
        } else {
            //Make all field un-editable:
            title.setValue(Optional.ofNullable(entity.getTitle()).orElse(""));
            subtitle.setValue(Optional.ofNullable(entity.getSubtitle()).orElse(""));
            email.setValue(Optional.ofNullable(entity.getEmail()).orElse(""));
            description.setValue(Optional.ofNullable(entity.getDescription()).orElse(""));
            add(title, subtitle, description, email);
        }
        //UI config:
        setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("300px", 2)
        );
        // UI Components config:
        description.setHelperText("Max = 256");
        description.setMaxLength(256);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e ->
                e.getSource().setHelperText(e.getValue().length() + "/" + 256)
        );
        // Stretch the username & actionBar field over 2 columns
        setColspan(email, 2);
        setColspan(description, 2);
        setColspan(actionBar, 2);
    }

    private ComponentEventListener<ClickEvent<Button>> onSaveAction() {
        return (event) -> {
            //Read from ui-component and fill entity:
            entity.setTitle(title.getValue());
            entity.setSubtitle(subtitle.getValue());
            entity.setDescription(description.getValue());
            entity.setEmail(email.getValue());
            //
            UI ui = event.getSource().getUI().orElse(null);
            Validator validator = ValidationConfig.getValidator();
            String messages = ValidationConfig.validateWithMessage(validator, entity);
            if (messages != null) {
                Notification notification = Notification.show(messages);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                this.actionBar.setSaveButtonEnable(true);
            } else {
                //TODO: Now save entity:
                if (eventType == EventType.UPDATE) {
                    AppQueue.dispatchTask(new DisplayAsyncNotification(ui
                            , "Update existing:" + entity.getTitle()));
                } else {
                    AppQueue.dispatchTask(new DisplayAsyncNotification(ui
                            , "Save New:" + entity.getTitle()));
                }
                dialog.close();
            }
        };
    }
}
