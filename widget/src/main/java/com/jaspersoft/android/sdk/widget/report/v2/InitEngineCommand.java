package com.jaspersoft.android.sdk.widget.report.v2;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class InitEngineCommand extends Command {
    private final Dispatcher dispatcher;
    private final RunOptions options;
    private final ServerInfoService infoService;

    InitEngineCommand(
            ServerInfoService infoService,
            Dispatcher dispatcher,
            RunOptions options
    ) {
        this.infoService = infoService;
        this.dispatcher = dispatcher;
        this.options = options;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    ServerInfo serverInfo = infoService.requestServerInfo();
                    double code = serverInfo.getVersion().code();
                    dispatcher.dispatch(new TransitToEngineEvent(code, options));
                } catch (ServiceException e) {
                    dispatcher.dispatch(e);
                }
                return null;
            }
        };
    }
}
