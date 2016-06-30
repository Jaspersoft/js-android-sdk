package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RunReportRestCommand extends Command {
    private static final String RUN_COMMAND = "javascript:MobileClient.getInstance().report().show(%s);";

    private final WebView webView;
    private final RunOptions runOptions;
    private final ReportExecutor reportExecutor;

    protected RunReportRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, RunOptions runOptions, ReportExecutor reportExecutor) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.runOptions = runOptions;
        this.reportExecutor = reportExecutor;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPreExecute() {
                if (runOptions.getDestination().getPage() == null) {
                    ServiceException anchorException = new ServiceException("Anchor is not supported for current server version", new Throwable("Anchor is not supported for current server version"), StatusCodes.EXPORT_ANCHOR_UNSUPPORTED);
                    dispatcher.dispatch(eventFactory.createErrorEvent(anchorException));
                    cancel(true);
                }
            }
            @Override
            protected String doInBackground(Object... params) {
                try {
                    reportExecutor.execute(runOptions.getReportUri(), runOptions.getParameters());
                    String page = reportExecutor.export(runOptions.getDestination().getPage());
                    return String.format(RUN_COMMAND, page);
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
                if (script == null) return;
                webView.loadUrl(script);
            }
        };
    }
}
