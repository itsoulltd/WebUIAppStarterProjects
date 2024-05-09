package com.infoworks.lab.domain.beans.queues;

import com.infoworks.lab.beans.queue.AbstractTaskQueue;
import com.infoworks.lab.beans.tasks.definition.Task;
import com.infoworks.lab.beans.tasks.definition.TaskQueue;
import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.rest.models.Message;
import com.vaadin.flow.component.UI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventQueue extends AbstractTaskQueue {

    protected static Logger LOG = Logger.getLogger(EventQueue.class.getSimpleName());
    public static final String X_EVENT_QUEUE = "X-Event-Queue";
    public static final String X_EVENT_EXECUTOR = "X-Event-Executor";
    public static final Integer MAX_POOL_COUNT = 10;

    public static void register() {
        Object old = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        if (old == null) {
            UI.getCurrent().getSession().setAttribute(X_EVENT_QUEUE, new EventQueue());
        }

        Object oldE = UI.getCurrent().getSession().getAttribute(X_EVENT_EXECUTOR);
        if (oldE == null) {
            UI.getCurrent().getSession().setAttribute(X_EVENT_EXECUTOR
                    , Executors.newFixedThreadPool(MAX_POOL_COUNT));
        }
    }

    public static void unregister() {
        UI.getCurrent().getSession().setAttribute(X_EVENT_QUEUE, null);
        //
        Object oldE = UI.getCurrent().getSession().getAttribute(X_EVENT_EXECUTOR);
        if (oldE != null) {
            try {
                ((ExecutorService)oldE).shutdown();
            } catch (Exception e) {
                LOG.log(Level.WARNING, e.getMessage());
            } finally {
                UI.getCurrent().getSession().setAttribute(X_EVENT_EXECUTOR, null);
            }
        }
    }

    public static void dispatchTask(Task aTask) {
        Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_EXECUTOR);
        if (evnQueue != null && evnQueue instanceof ExecutorService) {
            ((ExecutorService) evnQueue).submit(() -> aTask.execute(aTask.getMessage()));
        }
    }

    public static void dispatchTaskInQueue(Task aTask) {
        Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        if (evnQueue != null && evnQueue instanceof EventQueue) {
            ((EventQueue) evnQueue).add(aTask);
        }
    }

    /////////////////////Impl-EventQueue///////////////////

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

    /////////////////////End-EventQueue///////////////////
}
