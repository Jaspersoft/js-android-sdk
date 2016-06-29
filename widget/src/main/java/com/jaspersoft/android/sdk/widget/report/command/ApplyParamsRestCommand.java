package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ApplyParamsRestCommand extends Command {
    private static final String SHOW_COMMAND = "javascript:MobileClient.getInstance().report().show(%s);";

    private final WebView webView;
    private final ReportExecutor reportExecutor;
    private final List<ReportParameter> parameters;

    protected ApplyParamsRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, ReportExecutor reportExecutor, List<ReportParameter> parameters) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportExecutor = reportExecutor;
        this.parameters = parameters;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    reportExecutor.updateParams(parameters);
                    String page = reportExecutor.export(1);
                    return String.format(SHOW_COMMAND, page);
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                } catch (IOException e) {
                    ServiceException exception = new ServiceException("Can not render report", e, StatusCodes.REPORT_RENDER_FAILED);
                    dispatcher.dispatch(eventFactory.createErrorEvent(exception));
                }
                return null;
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
