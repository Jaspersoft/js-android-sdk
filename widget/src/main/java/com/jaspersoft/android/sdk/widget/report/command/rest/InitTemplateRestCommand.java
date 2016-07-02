package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitTemplateRestCommand extends Command {

    InitTemplateRestCommand(Dispatcher dispatcher, EventFactory eventFactory) {
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
