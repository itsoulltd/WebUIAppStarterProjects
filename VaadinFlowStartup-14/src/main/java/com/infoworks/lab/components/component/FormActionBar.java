package com.infoworks.lab.components.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class FormActionBar extends HorizontalLayout {

    private boolean activateSave; //By default false;
    private Dialog dialog;
    private Button save = new Button("SAVE", VaadinIcon.CHECK_CIRCLE.create());
    private Button close = new Button("CLOSE", VaadinIcon.CLOSE.create());

    public FormActionBar(Dialog dialog) {
        this.dialog = dialog;
        setSpacing(true);
        setPadding(true);
        setDefaultVerticalComponentAlignment(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    public FormActionBar() {
        this(null);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public boolean isActivateSave() {
        return activateSave;
    }

    public void updateSaveButton(String title, VaadinIcon vIcon) {
        if(title != null && !title.isEmpty()) this.save.setText(title);
        if(vIcon != null) this.save.setIcon(vIcon.create());
    }

    public void updateSaveButton(String title, VaadinIcon vIcon, ButtonVariant...themes) {
        updateSaveButton(title, vIcon);
        this.save.addThemeVariants(themes);
    }

    public void updateCloseButton(String title, VaadinIcon vIcon) {
        if(title != null && !title.isEmpty()) this.close.setText(title);
        if(vIcon != null) this.close.setIcon(vIcon.create());
    }

    public void updateCloseButton(String title, VaadinIcon vIcon, ButtonVariant...themes) {
        updateCloseButton(title, vIcon);
        this.close.addThemeVariants(themes);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //
        if (dialog != null) {
            this.close.addClickListener((e) -> dialog.close());
            if (isActivateSave()) {
                this.save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                add(this.close, this.save);
            } else {
                this.close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                add(this.close);
            }
        } else {
            if (isActivateSave()) {
                this.save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                add(this.save);
            }
        }
    }

    public void setSaveButtonDisableOnClick(boolean disableOnClick) {
        this.save.setDisableOnClick(disableOnClick);
    }

    public void setSaveButtonEnable(boolean enable) {
        this.save.setEnabled(enable);
    }

    public void addOnSaveAction(ComponentEventListener<ClickEvent<Button>> eventListener) {
        if (eventListener == null) return;
        this.activateSave = (eventListener != null);
        this.save.addClickListener(eventListener);
    }
}
