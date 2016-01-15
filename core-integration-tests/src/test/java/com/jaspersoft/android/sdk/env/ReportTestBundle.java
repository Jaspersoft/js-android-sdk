package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportTestBundle {
    private final String reportUri;
    private final Map<String, Set<String>> params;
    private final ServerTestBundle serverTestBundle;

    public ReportTestBundle(String reportUri, Map<String, Set<String>> params, ServerTestBundle serverTestBundle) {
        this.reportUri = reportUri;
        this.params = params;
        this.serverTestBundle = serverTestBundle;
    }

    public boolean hasParams() {
        return !params.isEmpty();
    }

    public List<ReportParameter> getParams() {
        List<ReportParameter> parameters = new ArrayList<>(params.size());
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            parameters.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return Collections.unmodifiableList(parameters);
    }

    public String getReportUri() {
        return reportUri;
    }

    public SpringCredentials getCredentials() {
        return serverTestBundle.getCredentials();
    }

    public AuthorizedClient getClient() {
        return serverTestBundle.getClient();
    }

    @Override
    public String toString() {
        return "ReportTestBundle{" +
                "server=" + serverTestBundle +
                "hasParams=" + hasParams() +
                ", reportUri='" + reportUri + '\'' +
                '}';
    }
}
