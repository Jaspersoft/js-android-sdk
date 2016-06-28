package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ApplyParamsVisCommand extends Command {
    private static final String APPLY_PARAMS_SCRIPT = "javascript:MobileClient.getInstance().report().applyParams(%s);";

    private final WebView webView;
    private final List<ReportParameter> parameters;

    protected ApplyParamsVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, List<ReportParameter> parameters) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.parameters = parameters;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                Map<String, Set<String>> reportParams = toMap(parameters);
                return String.format(APPLY_PARAMS_SCRIPT, toJson(reportParams));
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }

            private Map<String, Set<String>> toMap(List<ReportParameter> parameters) {
                if (parameters.isEmpty()) {
                    return Collections.emptyMap();
                }
                Map<String, Set<String>> params = new HashMap<String, Set<String>>(parameters.size());
                for (ReportParameter parameter : parameters) {
                    params.put(parameter.getName(), parameter.getValue());
                }
                return params;
            }

            private String toJson(Map<String, Set<String>> params) {
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, Set<String>>>() {
                }.getType();
                return gson.toJson(params, mapType);
            }
        };
    }
}
