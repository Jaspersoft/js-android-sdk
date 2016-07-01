package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class AnchorRestCommand extends Command<RestEventFactory> {
    AnchorRestCommand(Dispatcher dispatcher, RestEventFactory eventFactory) {
        super(dispatcher, eventFactory);
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                ServiceException anchorException = new ServiceException("Anchor navigation is not supported for current server version", new Throwable("Anchor navigation is not supported for current server version"), StatusCodes.EXPORT_ANCHOR_UNSUPPORTED);
                dispatcher.dispatch(eventFactory.createErrorEvent(anchorException));
                cancel(true);
                return null;
            }
        };
    }
}
