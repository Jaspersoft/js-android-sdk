package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RefreshUnsupportedCommand extends Command {
    RefreshUnsupportedCommand(Dispatcher dispatcher, EventFactory eventFactory) {
        super(dispatcher, eventFactory);
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Void, Void>() {
            @Override
            protected void onPreExecute() {
                throw new UnsupportedOperationException("Data refreshing is not supported for current server version");
            }

            @Override
            protected Void doInBackground(Object... params) {
                return null;
            }
        };
    }
}
