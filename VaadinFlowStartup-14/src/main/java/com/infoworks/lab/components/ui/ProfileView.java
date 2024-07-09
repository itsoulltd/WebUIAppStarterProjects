package com.infoworks.lab.components.ui;

import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.DisplayAsyncNotification;
import com.infoworks.lab.layouts.RootAppLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = RoutePath.PROFILE_VIEW, layout = RootAppLayout.class)
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
        //
        VerticalLayout root = new VerticalLayout();
        addSampleComponent(root);
        getContent().add(root);
    }

    private void addSampleComponent(VerticalLayout root) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);
        //
        TextField messageLabel = new TextField("", "Enter Message!");
        messageLabel.addValueChangeListener((event) -> message = event.getValue());
        layout.add(messageLabel);
        //
        layout.add(new Button("Async Event", (event) -> {
            //Example How to do async ui update in Vaadin:
            UI ui = event.getSource().getUI().orElse(null);
            EventQueue.dispatchTask(new DisplayAsyncNotification(ui, message));
        }));
        root.add(layout);
    }
}