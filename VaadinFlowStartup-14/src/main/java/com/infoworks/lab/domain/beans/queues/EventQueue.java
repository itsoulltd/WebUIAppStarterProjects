package com.infoworks.lab.domain.beans.queues;

import com.infoworks.lab.beans.queue.AbstractTaskQueue;
import com.infoworks.lab.beans.tasks.definition.Task;
import com.infoworks.lab.beans.tasks.definition.TaskQueue;
import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.rest.models.Message;
import com.vaadin.flow.component.UI;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventQueue extends AbstractTaskQueue {

    protected static Logger LOG = Logger.getLogger(EventQueue.class.getSimpleName());
    public static final String X_EVENT_QUEUE = "X-Event-Queue";
    public static final String X_EVENT_EXECUTOR = "X-Event-Executor";
    public static final String X_EVENT_SCHEDULER_EXECUTOR = "X-Event-Scheduler-Executor";
    public static final Integer MAX_POOL_COUNT = 10;

    public static void register() {
        Object old = UI.getCurrent().getSession().getAttribute(X_EVENT_QUEUE);
        if (old == null) {
            EventQueue queue = new EventQueue();
            queue.onTaskComplete((message, state) -> {
                if (message != null) {
                    LOG.log(Level.WARNING, message.toString());
                }
            });
            UI.getCurrent().getSession().setAttribute(X_EVENT_QUEUE, queue);
        }
        //
        Object oldE = UI.getCurrent().getSession().getAttribute(X_EVENT_EXECUTOR);
        if (oldE == null) {
            UI.getCurrent().getSession().setAttribute(X_EVENT_EXECUTOR
                    , Executors.newFixedThreadPool(MAX_POOL_COUNT));
        }
        //
        Object oldSE = UI.getCurrent().getSession().getAttribute(X_EVENT_SCHEDULER_EXECUTOR);
        if (oldSE == null) {
            UI.getCurrent().getSession().setAttribute(X_EVENT_SCHEDULER_EXECUTOR
                    , Executors.newScheduledThreadPool(MAX_POOL_COUNT));
        }
    }

    public static void unregister() {
        recycle(X_EVENT_QUEUE);
        recycle(X_EVENT_EXECUTOR);
        recycle(X_EVENT_SCHEDULER_EXECUTOR);
    }

    private static void recycle(String attributeKey) {
        Object exeQueue = UI.getCurrent().getSession().getAttribute(attributeKey);
        if (exeQueue != null) {
            if (exeQueue instanceof ExecutorService) {
                try {
                    ((ExecutorService)exeQueue).shutdown();
                } catch (Exception e) {
                    LOG.log(Level.WARNING, e.getMessage());
                }
            } else if (exeQueue instanceof EventQueue) {
                ((EventQueue) exeQueue).onTaskComplete((message, state) -> {});
            }
        }
        UI.getCurrent().getSession().setAttribute(attributeKey, null);
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

    public static void dispatch(int delay, TimeUnit unit, Callable callable) {
        if (delay > 0) {
            Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_SCHEDULER_EXECUTOR);
            if (evnQueue != null && evnQueue instanceof ScheduledExecutorService) {
                ((ScheduledExecutorService) evnQueue).schedule(callable, delay, unit);
            }
        } else {
            Object evnQueue = UI.getCurrent().getSession().getAttribute(X_EVENT_EXECUTOR);
            if (evnQueue != null && evnQueue instanceof ExecutorService) {
                ((ExecutorService) evnQueue).submit(callable);
            }
        }
    }

    //////////////////////////////////Impl-EventQueue/////////////////////////////////

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

    //////////////////////////////////End-EventQueue////////////////////////////////
}
