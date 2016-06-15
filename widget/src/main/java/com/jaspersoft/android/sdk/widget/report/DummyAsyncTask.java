package com.jaspersoft.android.sdk.widget.report;

import android.os.AsyncTask;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class DummyAsyncTask extends AsyncTask<Void, Void, Void> {
    static final DummyAsyncTask INSTANCE = new DummyAsyncTask();

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
}
