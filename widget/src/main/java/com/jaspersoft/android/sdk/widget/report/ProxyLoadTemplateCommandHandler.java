package com.jaspersoft.android.sdk.widget.report;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class ProxyLoadTemplateCommandHandler implements CommandHandler<LoadTemplateCommand> {
    private final Factory commandHandlerFactory;
    private AsyncTask task = DummyAsyncTask.INSTANCE;

    ProxyLoadTemplateCommandHandler(CommandHandler.Factory commandHandlerFactory) {
        this.commandHandlerFactory = commandHandlerFactory;
    }

    @Override
    public void handle(LoadTemplateCommand command) {
        task = new TemplateCreationTask(command);
        AsyncTaskCompat.executeParallel(task);
    }

    @Override
    public void cancel() {
        task.cancel(true);
    }

    private class TemplateCreationTask extends AsyncTask<Void, Void, CommandHandler<LoadTemplateCommand>> {
        private final LoadTemplateCommand command;

        private TemplateCreationTask(LoadTemplateCommand command) {
            this.command = command;
        }

        @Override
        protected CommandHandler<LoadTemplateCommand> doInBackground(Void... params) {
            RunOptions options = command.getOptions();
            AuthorizedClient client = options.getClient();
            ServerInfoService infoService = ServerInfoService.newService(client);

            try {
                ServerInfo serverInfo = infoService.requestServerInfo();
                double code = serverInfo.getVersion().code();
                return commandHandlerFactory.createLoadTemplateCommandHandler(code);
            } catch (ServiceException e) {
                // TODO handle network errors
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(CommandHandler<LoadTemplateCommand> handler) {
            handler.handle(command);
        }
    }
}
