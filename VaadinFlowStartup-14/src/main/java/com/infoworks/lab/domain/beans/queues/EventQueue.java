package com.infoworks.lab.domain.beans.queues;

import com.infoworks.lab.beans.queue.AbstractTaskQueue;
import com.infoworks.lab.beans.tasks.definition.Task;
import com.infoworks.lab.beans.tasks.definition.TaskQueue;
import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.rest.models.Message;
import com.vaadin.flow.component.UI;

import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class EventQueue extends AbstractTaskQueue {

    public static final String X_EVENT_QUEUE = "X-Event-Queue";

    public static void register() {
        Object old = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        if (old == null) {
            UI.getCurrent().getSession().setAttribute(X_EVENT_QUEUE, new EventQueue());
        }
    }

    public static void unregister() {
        Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        UI.getCurrent().getSession().setAttribute(X_EVENT_QUEUE, null);
    }

    public static void dispatchTask(Task aTask) {
        Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        if (evnQueue != null && evnQueue instanceof EventQueue) {
            ((EventQueue) evnQueue).add(aTask);
        }
    }

    private final TaskQueue exeQueue;

    public EventQueue(int numberOfThreads) {
        numberOfThreads = numberOfThreads <= 0
                ? (Runtime.getRuntime().availableProcessors() / 2)
                : numberOfThreads;
        this.exeQueue = TaskQueue.createAsync(Executors.newFixedThreadPool(numberOfThreads));
    }

    public EventQueue() {
        this(1);
    }

    @Override
    public void onTaskComplete(BiConsumer<Message, TaskStack.State> biConsumer) {
        exeQueue.onTaskComplete(biConsumer);
    }

    @Override
    public void abort(Task task, Message error) {
        task.setMessage(error);
        exeQueue.add(task);
    }

    @Override
    public TaskQueue add(Task task) {
        exeQueue.add(task);
        return this;
    }

    @Override
    public TaskQueue cancel(Task task) {
        exeQueue.cancel(task);
        return this;
    }
}
