package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ReportTestBundle {
    private final String reportUri;
    private Map<String, Set<String>> params;
    private final ServerTestBundle serverTestBundle;
    private final PendingTask<Map<String, Set<String>>> paramsRequest;

    public ReportTestBundle(String reportUri, PendingTask<Map<String, Set<String>>> paramsRequest, ServerTestBundle serverTestBundle) {
        this.reportUri = reportUri;
        this.paramsRequest = paramsRequest;
        this.serverTestBundle = serverTestBundle;
    }

    public boolean hasParams() {
        return !loadParamsLazily().isEmpty();
    }

    private Map<String, Set<String>> loadParamsLazily() {
        if (params == null) {
            try {
                params = paramsRequest.perform();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return params;
    }

    public List<ReportParameter> getParams() {
        Map<String, Set<String>> params = loadParamsLazily();
        List<ReportParameter> parameters = new ArrayList<>(params.size());
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            parameters.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return Collections.unmodifiableList(parameters);
    }

    public String getUri() {
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
