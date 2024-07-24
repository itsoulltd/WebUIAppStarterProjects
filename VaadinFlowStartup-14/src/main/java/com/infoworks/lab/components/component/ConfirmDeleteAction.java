package com.infoworks.lab.components.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDeleteAction extends VerticalLayout {

    private Dialog dialog;
    private FormActionBar actionBar;
    private Component message;

    public ConfirmDeleteAction(Dialog dialog, Component message) {
        this.dialog = dialog;
        this.message = message;
        this.actionBar = new FormActionBar(dialog);
        setSpacing(true);
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public boolean isActivateSave() {
        return this.actionBar.isActivateSave();
    }

    public void updateDeleteButton(String title, VaadinIcon vIcon) {
        this.actionBar.updateSaveButton(title, vIcon);
    }

    public void updateCloseButton(String title, VaadinIcon vIcon) {
        this.actionBar.updateCloseButton(title, vIcon);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        actionBar.updateSaveButton("DELETE", VaadinIcon.TRASH, ButtonVariant.LUMO_ERROR);
        actionBar.updateCloseButton("CLOSE", VaadinIcon.CLOSE, ButtonVariant.LUMO_PRIMARY);
        add(message, actionBar);
    }

    public void setDeleteButtonDisableOnClick(boolean disableOnClick) {
        this.actionBar.setSaveButtonDisableOnClick(disableOnClick);
    }

    public void setDeleteButtonEnable(boolean enable) {
        this.actionBar.setSaveButtonEnable(enable);
    }

    public void addOnDeleteAction(ComponentEventListener<ClickEvent<Button>> eventListener) {
        if (eventListener == null) return;
        this.actionBar.addOnSaveAction(eventListener);
    }

}
