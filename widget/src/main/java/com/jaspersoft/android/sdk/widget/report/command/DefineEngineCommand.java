package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.Event;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class DefineEngineCommand extends Command {
    private final ServerInfoService infoService;

    public DefineEngineCommand(Dispatcher dispatcher, EventFactory eventFactory, ServerInfoService infoService) {
        super(dispatcher, eventFactory);
        this.infoService = infoService;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    ServerInfo serverInfo = infoService.requestServerInfo();
                    double code = serverInfo.getVersion().code();
                    boolean isPro = serverInfo.isEditionPro();
                    Event engineDefinedEvent = eventFactory.createEngineDefinedEvent(code, isPro);

                    dispatcher.dispatch(engineDefinedEvent);
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                }
                return null;
            }
        };
    }
}
