package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public abstract class Command<EV extends EventFactory> {
    protected final Dispatcher dispatcher;
    protected final EV eventFactory;

    private AsyncTask task;

    protected Command(Dispatcher dispatcher, EV eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }

    public void execute() {
        task = createTask();
        AsyncTaskCompat.executeParallel(task);
    }

    public void cancel() {
        task.cancel(true);
    }

    protected abstract AsyncTask createTask();
}
