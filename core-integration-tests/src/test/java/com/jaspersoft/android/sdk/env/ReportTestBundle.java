package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportTestBundle {
    private final String reportUri;
    private final ServerTestBundle serverTestBundle;

    public ReportTestBundle(String reportUri, ServerTestBundle serverTestBundle) {
        this.reportUri = reportUri;
        this.serverTestBundle = serverTestBundle;
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
                ", reportUri='" + reportUri + '\'' +
                '}';
    }
}
