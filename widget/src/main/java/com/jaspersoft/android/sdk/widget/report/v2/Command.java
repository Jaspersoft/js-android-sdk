package com.jaspersoft.android.sdk.widget.report.v2;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

/**
 * @author Tom Koptel
 * @since 2.6
 */
abstract class Command {

    private AsyncTask task;

    public void execute() {
        task = createTask();
        AsyncTaskCompat.executeParallel(task);
    }

    public void cancel() {
        task.cancel(true);
    }

    protected abstract AsyncTask createTask();
}
