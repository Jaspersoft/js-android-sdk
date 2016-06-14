package com.jaspersoft.android.sdk.widget.report;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class AsyncTaskExecutor extends TaskExecutor {
    static TaskExecutor INSTANCE = new AsyncTaskExecutor();

    private AsyncTaskExecutor() {}

    @Override
    void perform(final Task task) {
        AsyncTaskCompat.executeParallel(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                task.execute();
                return null;
            }
        });
    }
}
