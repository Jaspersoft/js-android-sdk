package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandFactory {
    private final Dispatcher dispatcher;
    private final PresenterState.Context context;

    public CommandFactory(Dispatcher dispatcher, PresenterState.Context context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    public Command createEngineInitCommand(RunOptions options) {
        AuthorizedClient client = context.getClient();
        ServerInfoService infoService = ServerInfoService.newService(client);
        return new InitEngineCommand(infoService, dispatcher, options);
    }
}
