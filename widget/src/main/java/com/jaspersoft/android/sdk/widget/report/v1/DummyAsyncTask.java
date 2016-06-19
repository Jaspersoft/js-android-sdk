package com.jaspersoft.android.sdk.widget.report.v1;

import android.os.AsyncTask;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class DummyAsyncTask extends AsyncTask<Object, Object, Object> {
    static final DummyAsyncTask INSTANCE = new DummyAsyncTask();

    @Override
    protected Object doInBackground(Object... params) {
        return null;
    }
}
