package com.jaspersoft.android.sdk.widget.report.renderer.command;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class AnchorUnsupportedCommand extends Command {
    AnchorUnsupportedCommand(Dispatcher dispatcher, EventFactory eventFactory) {
        super(dispatcher, eventFactory);
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Void, Void>() {
            @Override
            protected void onPreExecute() {
                throw new UnsupportedOperationException("Anchor navigation is not supported for current server version");
            }

            @Override
            protected Void doInBackground(Object... params) {
                return null;
            }
        };
    }
}
