package com.infoworks.lab.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.ExecutableTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

public class DisplayAsyncNotification extends ExecutableTask<Message, Response> {

    private UI ui;
    private Notification.Position position = Notification.Position.TOP_CENTER;

    public DisplayAsyncNotification(UI ui, String message) {
        this(ui, 1000, 1000, Notification.Position.TOP_CENTER, message);
    }

    public DisplayAsyncNotification(UI ui
            , int afterInMillis
            , int forInMillis
            , Notification.Position position
            , String message) {
        super(new Property("message", message)
                , new Property("afterInMillis", afterInMillis)
                , new Property("forInMillis", forInMillis));
        this.ui = ui;
        this.position = position;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        String msgStr = getPropertyValue("message").toString();
        int delay = (int) getPropertyValue("afterInMillis");
        int duration = (int) getPropertyValue("forInMillis");
        System.out.println("Display: " + msgStr);
        try {
            Thread.sleep(delay);
            if (ui != null) {
                ui.access(() -> Notification.show(msgStr, duration, position));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Response().setStatus(200);
    }
}
