package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitTemplateRestCommand extends Command {

    protected InitTemplateRestCommand(Dispatcher dispatcher, EventFactory eventFactory) {
        super(dispatcher, eventFactory);
    }

    @Override
    protected final AsyncTask createTask() {
        return new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                dispatcher.dispatch(eventFactory.createTemplateInitedEvent());
                return null;
            }
        };
    }
}
