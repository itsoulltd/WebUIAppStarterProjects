package com.infoworks.lab.components.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class IndeterminateProgressDialog extends Dialog {

    private String message;

    public IndeterminateProgressDialog(String message) {
        this.message = message;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ProgressBar bar = new ProgressBar();
        bar.setIndeterminate(true);
        Div label = new Div();
        label.setText(getMessage());
        add(label, bar);
    }

    public String getMessage() {
        return message;
    }
}
